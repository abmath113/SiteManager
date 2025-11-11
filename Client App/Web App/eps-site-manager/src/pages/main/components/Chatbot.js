import React, { useState, useEffect } from 'react';
import ReactMarkdown from 'react-markdown';

// const API_URL = 'http://192.168.202.105:8090/api/proxy/query';

   const API_URL = 'http://localhost:8090/epssitemanagerapi/api/proxy/query';

function Chatbot() {
  const [userQuery, setUserQuery] = useState('');
  const [response, setResponse] = useState('');
  const [loading, setLoading] = useState(false);
  const [isSuccess, setIsSuccess] = useState(null); // null = default, true = success, false = error
  const [token, setToken] = useState('');

  // Get the JWT token from localStorage when component mounts
  useEffect(() => {
    const jwtToken = localStorage.getItem('token');
    if (jwtToken) {
      setToken(jwtToken);
    }
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setResponse('');

    try {
      // Get the latest token from localStorage (in case it was updated)
      const currentToken = localStorage.getItem('token');
      
      const res = await fetch(API_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${currentToken || token}` // Add token as Bearer token
        },
        body: JSON.stringify({ query: userQuery }),
      });
    
      if (!res.ok) {
        const errorData = await res.json();
        setIsSuccess(false);
        setResponse(`Error: ${errorData.error || 'Something went wrong.'}`);
      } else {
        const data = await res.json();
        
        setIsSuccess(!data.error);
        setResponse(data.error ? `Error: ${data.error}` : data.response);
      }
    } catch (error) {
      console.error("Error processing query:", error);
      setIsSuccess(false);
      setResponse("Server connection error. Check your internet and try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      fontFamily: '"Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif',
      maxWidth: '800px',
      margin: '0 auto',
      padding: '2rem 1rem',
      backgroundColor: '#ffffff',
      minHeight: '100vh',
      color: '#333',
    }}>
      <header style={{
        textAlign: 'center',
        marginBottom: '2rem',
      }}>
        <h1 style={{
          fontSize: '2.5rem',
          margin: 0,
          background: 'linear-gradient(135deg, #2c3e50, #4a6983)',
          WebkitBackgroundClip: 'text',
          WebkitTextFillColor: 'transparent',
          fontWeight: 'bold',
        }}>Site Manager AI</h1>
        <p style={{ color: '#7f8c8d', fontSize: '1rem' }}>Ask about Data....</p>
      </header>

      <main style={{ width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        <form onSubmit={handleSubmit} style={{ width: '100%', marginBottom: '2rem' }}>
          <div style={{
            display: 'flex',
            borderRadius: '10px',
            overflow: 'hidden',
            boxShadow: '0 2px 10px rgba(0,0,0,0.1)',
            width: '100%',
          }}>
            <input
              type="text"
              maxLength="150"
              value={userQuery}
              onChange={(e) => setUserQuery(e.target.value)}
              placeholder="Ask something..."
              style={{
                flex: 1,
                padding: '1rem',
                border: `2px solid ${
                  isSuccess === null ? '#ccc' : isSuccess ? 'green' : 'red'
                }`,
                outline: 'none',
                backgroundColor: '#f1f3f5',
                fontSize: '1rem',
                transition: 'border-color 0.3s ease',
              }}
              
              required
            />
          </div>
        </form>

        {/* Button aligned to the right */}
        <div style={{ width: '100%', display: 'flex', justifyContent: 'flex-end' }}>
          <button
            type="submit"
            onClick={handleSubmit}
            disabled={loading}
            style={{
              backgroundColor: loading ? '#b2bec3' : '#3498db',
              color: '#fff',
              border: 'none',
              borderRadius: '20px', // Slightly less rounded for a more natural look
              padding: '0.75rem 2rem', // Balanced padding
              fontWeight: 'bold',
              cursor: loading ? 'not-allowed' : 'pointer',
              transition: 'background-color 0.3s ease',
              fontSize: '1.125rem', // A more moderate font size increase
              lineHeight: '1.5', // Ensures the button text stays visually centered
            }}
          >
            {loading ? 'Loading...' : 'Ask'}
          </button>
        </div>

        {loading && (
          <div style={{
            width: '100%',
            height: '120px',
            borderRadius: '12px',
            background: '#f8f9fa',
            position: 'relative',
            overflow: 'hidden',
            marginBottom: '2rem',
          }}>
            <div style={{
              position: 'absolute',
              top: 0,
              left: '-150%',
              width: '200%',
              height: '100%',
              backgroundImage: 'linear-gradient(120deg, transparent 0%, rgba(116, 124, 183, 0.24) 50%, transparent 100%)',
              animation: 'glitter 1s infinite linear',
            }} />
            <p style={{
              position: 'relative',
              textAlign: 'center',
              paddingTop: '10rem',
              color: '#7f8c8d',
              fontSize: '1rem',
              zIndex: 1,
            }}>Let me find the answers 💭 ...</p>
            <style>{`
              @keyframes glitter {
                0% { left: -150%; }
                50% { left: -50%; }
                100% { left: 150%; }
              }
            `}</style>
          </div>
        )}

        {response && !loading && (
          <div style={{
            width: '100%',
            padding: '2rem',
            borderRadius: '12px',
            backgroundColor: '#f8f9fa',
            boxShadow: '0 2px 8px rgba(0,0,0,0.05)',
            animation: 'fadeIn 0.4s ease-in-out',
          }}>
            <h2 style={{ fontSize: '1.2rem', marginBottom: '1rem' }}>Response:</h2>
            <div style={{
              backgroundColor: '#fff',
              padding: '1rem',
              borderRadius: '8px',
              lineHeight: '1.6',
            }}>
              <ReactMarkdown>{response}</ReactMarkdown>
            </div>
            <style>{`
              @keyframes fadeIn {
                from { opacity: 0; transform: translateY(10px); }
                to { opacity: 1; transform: translateY(0); }
              }
            `}</style>
          </div>
        )}

        <footer style={{
          marginTop: '2rem',
          fontSize: '0.8rem',
          color: '#7f8c8d',
          textAlign: 'center',
        }}>
          <p><strong>Note:</strong> AI-generated slop can contain mistakes. Always verify with the original data.</p>
        </footer>
      </main>
    </div>
  );
}

export default Chatbot;