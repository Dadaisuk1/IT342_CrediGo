import React, { useState } from 'react';
import toast from 'react-hot-toast';

const CreateProduct = () => {
  const [product, setProduct] = useState({
    productname: '',
    description: '',
    price: '',
    salePrice: '',
    imageUrl: '',
    isActive: true,
  });

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setProduct((prev) => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem('token');

      const res = await fetch('http://it342-credigo-msd3.onrender.com/api/products/createProduct', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          productname: product.productname,
          description: product.description,
          price: product.price,
          salePrice: product.salePrice,
          imageUrl: product.imageUrl,
          isActive: product.isActive,
        }),
      });

      if (!res.ok) {
        const errorText = await res.text();
        throw new Error(errorText || 'Failed to create product');
      }

      toast.success('Product created successfully!');
      setProduct({
        productname: '',
        description: '',
        price: '',
        salePrice: '',
        imageUrl: '',
        isActive: true,
      });
    } catch (error) {
      console.error('Product creation error:', error);
      toast.error(error.message || 'Failed to create product. Please try again.');
    }
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Create New Product</h1>
      <form onSubmit={handleSubmit} className="space-y-4 max-w-xl">
        <input name="productname" value={product.productname} onChange={handleChange} placeholder="Product Name" className="w-full p-2 border" required />
        <input name="description" value={product.description} onChange={handleChange} placeholder="Description" className="w-full p-2 border" required />
        <input name="price" type="number" value={product.price} onChange={handleChange} placeholder="Price" className="w-full p-2 border" required />
        <input name="salePrice" type="number" value={product.salePrice} onChange={handleChange} placeholder="Sale Price (optional)" className="w-full p-2 border" />
        <input name="imageUrl" value={product.imageUrl} onChange={handleChange} placeholder="Image URL" className="w-full p-2 border" required />
        <label className="flex items-center gap-2">
          <input type="checkbox" name="isActive" checked={product.isActive} onChange={handleChange} />
          Active
        </label>
        <button type="submit" className="bg-green-600 text-white px-4 py-2 rounded">Create Product</button>
      </form>
    </div>
  );
};

export default CreateProduct;
