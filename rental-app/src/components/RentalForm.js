import React, { useState } from 'react';
import axios from 'axios';

const RentalForm = () => {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [price, setPrice] = useState('');

  const handleSubmit = (event) => {
    event.preventDefault();
    axios.post('http://localhost:8080/MotelBE/api/motels', { title, description, price })
      .then(response => console.log('Rental added:', response.data))
      .catch(error => console.error('Error adding rental:', error));
  };

  return (
    <form onSubmit={handleSubmit}>
      <label>
        Title:
        <input type="text" value={title} onChange={e => setTitle(e.target.value)} />
      </label>
      <label>
        Description:
        <textarea value={description} onChange={e => setDescription(e.target.value)}></textarea>
      </label>
      <label>
        Price:
        <input type="number" value={price} onChange={e => setPrice(e.target.value)} />
      </label>
      <button type="submit">Add Rental</button>
    </form>
  );
};

export default RentalForm;
