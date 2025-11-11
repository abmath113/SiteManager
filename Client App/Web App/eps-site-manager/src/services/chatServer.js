// server.js
const express = require('express');
const { Pool } = require('pg');
const cors = require('cors');
const bodyParser = require('body-parser');
const fs = require('fs');
const path = require('path');
const { GoogleGenerativeAI } = require('@google/generative-ai');
const OpenAI = require('openai');
 
require('dotenv').config();
 
const app = express();
const port = process.env.PORT || 5000;
 
// Configure middleware
app.use(cors());
app.use(bodyParser.json());
 
// Initialize AI APIs
const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY);
const openai = new OpenAI({
  apiKey: process.env.OPENAI_API_KEY
});
 
// PostgreSQL connection pool
const pool = new Pool({
  host: process.env.PG_HOST || 'localhost',
  port: process.env.PG_PORT || 5432,
  database: process.env.PG_DATABASE || 'mydatabase',
  user: process.env.PG_USER || 'postgres',
  password: process.env.PG_PASSWORD || 'postgres',
});
 
// Database Schema Information (Hardcoded)
const DB_SCHEMA = `
Database Schema:
bank_master (
  bank_id               SERIAL         PK
  bank_code             VARCHAR(10)
  bank_name             VARCHAR(50)
)
 
channel_manager_master (
  channel_manager_id    SERIAL         PK
  channel_manager_name  VARCHAR(255)
  email_id              VARCHAR(255)
  phone_no              VARCHAR(255)
  status                BOOLEAN
)
 
site_master (
  site_id                       SERIAL   PK
  site_code                     VARCHAR(30)
  site_address                  VARCHAR(500)
  site_area                     INTEGER
  site_status                   BOOLEAN
  bank_bank_id                  INTEGER  → bank_master.bank_id
  channel_manager_channel_manager_id INTEGER → channel_manager_master.channel_manager_id
)
 
landlord_master (
  landlord_id          SERIAL         PK
  name                 VARCHAR(255)
  aadhar_no            VARCHAR(20)
  pan                  VARCHAR(10)
  mobile_no            VARCHAR(15)
  address              VARCHAR(500)
  beneficiary_name     VARCHAR(255)
  account_no           VARCHAR(20)
  ifsc_code            VARCHAR(11)
  status               BOOLEAN
  created_by           VARCHAR(255)
  created_on           TIMESTAMP
)
 
rent_agreement_master (
  agreement_id            SERIAL      PK
  agreement_date          DATE
  agreement_end_date      DATE
  agreement_span          INTEGER
  consider_agreement_date DATE
  deposit                 INTEGER     NOT NULL
  escalation_after_months INTEGER
  escaalation_percent     INTEGER
  monthly_rent            INTEGER
  payment_interval        INTEGER
  rent_pay_start_date     DATE
  solar_panel_rent        INTEGER
  rent_agreement_status   BOOLEAN
  termination_date        DATE
  termination_remark      VARCHAR(255)
  landlord_id_landlord_id INTEGER     → landlord_master.landlord_id
  site_id_site_id         INTEGER     → site_master.site_id
)
 
site_rent_records (
  site_rent_record_id   SERIAL         PK
  agreement_id_agreement_id INTEGER     → rent_agreement_master.agreement_id
  rent_month            VARCHAR(255)
  payment_date          VARCHAR(255)
  amount_paid           INTEGER
  generated_rent        INTEGER
  utr_no                VARCHAR(255)
  transaction_status    BOOLEAN
  reason                VARCHAR(4000)
  remarks               VARCHAR(4000)
)
`;
 
// Ensure log directory exists
const logDir = path.join( __dirname  , 'logs');
if (!fs.existsSync(logDir)) {
  fs.mkdirSync(logDir);
}
 
// Function to log query to file
const logQuery = (message, type = 'INFO', provider = 'SYSTEM') => {
  const timestamp = new Date().toISOString();
  const logEntry = `[${timestamp}] [${type}] [${provider}] ${message}\n`;
  const logFile = path.join(logDir, 'sql-queries.log');
 
  fs.appendFile(logFile, logEntry, (err) => {
    if (err) {
      console.error('Error writing to log file:', err);
    }
  });
  
  // Replace chalk with standard console.log
  const timestampStr = `[${timestamp}]`;
  const typeStr = `[${type}]`;
  const providerStr = `[${provider}]`;
  
  // Log to console with standard format
  console.log(`${timestampStr} ${typeStr} ${providerStr} ${message}`);
};
 
// Function to check if query contains restricted operations
const containsRestrictedOperations = (query) => {
  const restrictedRegex = /DROP|DELETE|TRUNCATE|ALTER|UPDATE|INSERT/i;
  return restrictedRegex.test(query);
};
 
// Endpoint to process natural language queries
app.post('/api/query', async (req, res) => {
  try {
    const { query, provider = 'gemini' } = req.body;
   
    logQuery(`Received query request from provider: ${provider}`, 'INFO', 'API');
    logQuery(`User query: ${query}`, 'INFO', 'API');
   
    if (!query) {
      logQuery('Empty query received', 'ERROR', 'API');
      return res.status(400).json({ error: 'Query is required' });
    }
 
    // Check database connection
    try {
      const client = await pool.connect();
      logQuery('Database connection successful', 'INFO', 'DB');
      client.release();
    } catch (dbError) {
      logQuery(`Database connection failed: ${dbError.message}`, 'ERROR', 'DB');
      return res.status(500).json({ error: 'Database connection failed' });
    }
 
    // Generate SQL from natural language using selected provider
    logQuery(`Generating SQL using ${provider}`, 'INFO', provider.toUpperCase());
    const sqlResult = provider.toLowerCase() === 'openai'
      ? await generateSQLWithOpenAI(query)
      : await generateSQLWithGemini(query);
   
    if (sqlResult.error) {
      logQuery(`SQL generation failed: ${sqlResult.error}`, 'ERROR', provider.toUpperCase());
      return res.status(400).json({ error: sqlResult.error });
    }
 
    logQuery(`Generated SQL query: ${sqlResult.query}`, 'INFO', provider.toUpperCase());
 
    // Check if query is safe (SELECT only)
    if (containsRestrictedOperations(sqlResult.query)) {
      logQuery(`Restricted operations detected in query: ${sqlResult.query}`, 'ERROR', 'SECURITY');
      return res.status(400).json({
        error: 'Generated query contains restricted operations. Only SELECT queries are allowed.'
      });
    }
 
    // Execute the query
    const client = await pool.connect();
    try {
      logQuery('Executing SQL query on database', 'INFO', 'DB');
      const result = await client.query(sqlResult.query);
      logQuery(`Query executed successfully. Rows returned: ${result.rows.length}`, 'INFO', 'DB');
     
      // Generate natural language response based on provider
      logQuery('Generating natural language response', 'INFO', provider.toUpperCase());
      const naturalResponse = provider.toLowerCase() === 'openai'
        ? await generateResponseWithOpenAI(result.rows, query)
        : await generateResponseWithGemini(result.rows, query);
     
      logQuery('Response generation completed', 'INFO', provider.toUpperCase());
      return res.json({ response: naturalResponse });
    } catch (dbError) {
      logQuery(`Database query execution failed: ${dbError.message}`, 'ERROR', 'DB');
      throw dbError;
    } finally {
      client.release();
    }
  } catch (error) {
    logQuery(`Error processing query: ${error.message}`, 'ERROR', 'SYSTEM');
    return res.status(500).json({ error: 'An error occurred while processing your request' });
  }
});
 
// Function to generate SQL from natural language using Gemini
async function generateSQLWithGemini(naturalLanguageQuery) {
  try {
    logQuery('Initializing Gemini model', 'INFO', 'GEMINI');
    const model = genAI.getGenerativeModel({ model: "gemini-2.0-flash-lite" });
   
    const prompt = `
You are a SQL expert tasked with converting natural language queries to SQL SELECT statements for PostgreSQL.
${DB_SCHEMA}
 
Only generate SELECT queries. Never generate DROP, DELETE, TRUNCATE, ALTER, UPDATE, or INSERT statements.
Provide only the raw SQL query without any explanation, comments, or markdown formatting.
Do not include any backticks  or code blocks.
Just return the plain SQL query string that can be directly executed.
 
User Query: ${naturalLanguageQuery}
 
SQL Query:`;
 
    logQuery('Sending prompt to Gemini', 'INFO', 'GEMINI');
    const result = await model.generateContent(prompt);
    const response = result.response;
   
    // Get the raw text
    let sqlQuery = response.text().trim();
    logQuery(`Received response from Gemini: ${sqlQuery}`, 'INFO', 'GEMINI');
   
    // Remove any markdown code formatting (backticks)
    sqlQuery = sqlQuery.replace(/```sql/gi, '').replace(/```/g, '').trim();
   
    // Additional safety check for empty queries
    if (!sqlQuery) {
      logQuery('Generated SQL query is empty', 'ERROR', 'GEMINI');
      return { error: "Generated SQL query is empty" };
    }
   
    return { query: sqlQuery };
  } catch (error) {
    logQuery(`Error generating SQL with Gemini: ${error.message}`, 'ERROR', 'GEMINI');
    return { error: "Failed to generate SQL query with Gemini." };
  }
}
 
// Function to generate SQL from natural language using OpenAI
async function generateSQLWithOpenAI(naturalLanguageQuery) {
  try {
    logQuery('Initializing OpenAI request', 'INFO', 'OPENAI');
    const response = await openai.chat.completions.create({
      model: "gpt-4o-mini",
      messages: [
        {
          role: "system",
          content: `You are a SQL expert tasked with converting natural language queries to SQL SELECT statements for PostgreSQL.
${DB_SCHEMA}
 
Only generate SELECT queries. Never generate DROP, DELETE, TRUNCATE, ALTER, UPDATE, or INSERT statements.
Provide only the raw SQL query without any explanation, comments, or markdown formatting.
Do not include any backticks (\`\`\`) or code blocks.
Just return the plain SQL query string that can be directly executed.`
        },
        {
          role: "user",
          content: `User Query: ${naturalLanguageQuery}\n\nSQL Query:`
        }
      ],
      temperature: 0,
      max_tokens: 500
    });
   
    // Get the raw text
    let sqlQuery = response.choices[0].message.content.trim();
    logQuery(`Received response from OpenAI: ${sqlQuery}`, 'INFO', 'OPENAI');
   
    // Remove any markdown code formatting (backticks)
    sqlQuery = sqlQuery.replace(/```sql/gi, '').replace(/```/g, '').trim();
   
    // Additional safety check for empty queries
    if (!sqlQuery) {
      logQuery('Generated SQL query is empty', 'ERROR', 'OPENAI');
      return { error: "Generated SQL query is empty" };
    }
   
    return { query: sqlQuery };
  } catch (error) {
    logQuery(`Error generating SQL with OpenAI: ${error.message}`, 'ERROR', 'OPENAI');
    return { error: "Failed to generate SQL query with OpenAI." };
  }
}
 
// Function to generate natural language response from query results with Gemini
async function generateResponseWithGemini(results, originalQuery) {
  try {
    logQuery('Initializing Gemini for response generation', 'INFO', 'GEMINI');
    const model = genAI.getGenerativeModel({ model: "gemini-2.0-flash-lite" });
   
    // Format the results as JSON string
    const resultsJson = JSON.stringify(results, null, 2);
   
    const prompt = `
You are an assistant that explains database query results in natural language.
Original user question: "${originalQuery}"
 
Query results:
${resultsJson}
 
Please provide clear, concise results in natural language.
If the results are empty, explain that no matching data was found.
`;
 
    logQuery('Sending results to Gemini for natural language conversion', 'INFO', 'GEMINI');
    const result = await model.generateContent(prompt);
    const response = result.response;
    const naturalResponse = response.text();
    logQuery('Received natural language response from Gemini', 'INFO', 'GEMINI');
    return naturalResponse;
  } catch (error) {
    logQuery(`Error generating response with Gemini: ${error.message}`, 'ERROR', 'GEMINI');
    return "I had trouble interpreting the results. Please try rephrasing your question.";
  }
}
 
// Function to generate natural language response from query results with OpenAI
async function generateResponseWithOpenAI(results, originalQuery) {
  try {
    logQuery('Initializing OpenAI for response generation', 'INFO', 'OPENAI');
    // Format the results as JSON string
    const resultsJson = JSON.stringify(results, null, 2);
   
    logQuery('Sending results to OpenAI for natural language conversion', 'INFO', 'OPENAI');
    const response = await openai.chat.completions.create({
      model: "gpt-4o-mini",
      messages: [
        {
          role: "system",
          content: `You are an assistant that explains database query results in natural language.`
        },
        {
          role: "user",
          content: `Original user question: "${originalQuery}"
 
Query results:
${resultsJson}
 
Please provide  clear, concise results in natural language.
If the results are empty, explain that no matching data was found.`
        }
      ],
      temperature: 0.7,
      max_tokens: 500
    });
   
    const naturalResponse = response.choices[0].message.content;
    logQuery('Received natural language response from OpenAI', 'INFO', 'OPENAI');
    return naturalResponse;
  } catch (error) {
    logQuery(`Error generating response with OpenAI: ${error.message}`, 'ERROR', 'OPENAI');
    return "I had trouble interpreting the results. Please try rephrasing your question.";
  }
}
 
// Serve static assets if in production
if (process.env.NODE_ENV === 'production') {
  // Set static folder
  app.use(express.static('build'));
 
  app.get('*', (req, res) => {
    res.sendFile(path.resolve(__dirname, 'build', 'index.html'));
  });
} else {
  // In development, provide a simple response at the root
  app.get('/', (req, res) => {
    res.send('Natural Language to SQL API server is running. Use the React app to interact with it.');
  });
}
 
// Start the server
app.listen(port, () => {
  console.log(`Server running on port ${port}`);
});