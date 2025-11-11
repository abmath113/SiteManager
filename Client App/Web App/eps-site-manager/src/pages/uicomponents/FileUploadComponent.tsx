import React, { useState } from 'react';

interface FileUploadProps {
  onFileSelect: (file: File | null) => void;
}

const FileUploadComponent: React.FC<FileUploadProps> = ({ onFileSelect }) => {
  const [selectedFile, setSelectedFile] = useState<File | null>(null);

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files ? event.target.files[0] : null;
    setSelectedFile(file);
    onFileSelect(file); // Send the selected file to parent component
  };

  return (
    <div className="mb-3">
      <label htmlFor="fileUpload" className="form-label">
        Upload Agreement Scan (PDF)
      </label>
      <input
        type="file"
        id="fileUpload"
        accept=".pdf"
        className="form-control"
        onChange={handleFileChange}
      />
      {selectedFile && (
        <p className="mt-2">Selected file: {selectedFile.name}</p>
      )}
    </div>
  );
};

export default FileUploadComponent;
