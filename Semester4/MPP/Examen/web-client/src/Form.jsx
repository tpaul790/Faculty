import React, { useState } from 'react';

function Form() {
  const [id, setId] = useState(0);
  const [a1, setA1] = useState('');
  const [a2, setA2] = useState('');
  const [a3, setA3] = useState('');
  const [a4, setA4] = useState('');
  const [a5, setA5] = useState('');
  const [a6, setA6] = useState('');
  const [a7, setA7] = useState('');
  const [a8, setA8] = useState('');
  const [message, setMessage] = useState(null);
  const animals = [a1,a2,a3,a4,a5,a6,a7,a8];


  const handleUpdate = async (e) => {
    e.preventDefault();

const payload = {
  animals: animals // animals e deja o listă de stringuri
};

try {
  const response = await fetch(`http://localhost:8080/ro/mpp/configurations/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
  });

  if (response.ok) {
    setMessage('Configuration successfully updated');
    setA1(''); setA2(''); setA3(''); setA4(''); setA5(''); setA6(''); setA7(''); setA8('');
    setId(0);
  } else {
    setMessage('Error updating configuration');
  }
} catch (error) {
  setMessage('Error when connecting to the server');
  console.error(error);
}
  }


  return (
    <div>
      <h2>Form</h2>
      <form onSubmit={handleUpdate}>
        <label>ConfigurationId:</label>
        <input type="number" min={0} value={id} onChange={(e)=>setId(e.target.value)} required />
        <label>A1:</label><input value={a1} onChange={(e)=>setA1(e.target.value)} required />
        <label>A2:</label><input value={a2} onChange={(e)=>setA2(e.target.value)} required />
        <label>A3:</label><input value={a3} onChange={(e)=>setA3(e.target.value)} required />
        <label>A4:</label><input value={a4} onChange={(e)=>setA4(e.target.value)} required />
        <label>A5:</label><input value={a5} onChange={(e)=>setA5(e.target.value)} required />
        <label>A6:</label><input value={a6} onChange={(e)=>setA6(e.target.value)} required />
        <label>A7:</label><input value={a7} onChange={(e)=>setA7(e.target.value)} required />
        <label>A8:</label><input value={a8} onChange={(e)=>setA8(e.target.value)} required />
        <button type="submit">Update</button>
      </form>
      {message && <p>{message}</p>}
    </div>
  );
}

export default Form;
