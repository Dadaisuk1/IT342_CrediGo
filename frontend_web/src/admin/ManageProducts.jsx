import React, { useEffect, useState } from 'react';
import toast from 'react-hot-toast';

const ManageProducts = () => {
  const [products, setProducts] = useState([]);
  const [editProductId, setEditProductId] = useState(null);
  const [editForm, setEditForm] = useState({});
  const token = localStorage.getItem('token');
  console.log("📦 JWT Token:", token);
  const fetchProducts = async () => {
    try {
      const res = await fetch('http://it342-credigo-msd3.onrender.com/api/products/getAllProducts', {
        headers: { Authorization: `Bearer ${token}` }
      });
      const data = await res.json();
      console.log('✅ Products fetched:', data); // debug log
      setProducts(data);
    } catch (error) {
      console.error('Error fetching products:', error);
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this product?')) return;

    try {
      const res = await fetch(`http://it342-credigo-msd3.onrender.com/api/products/deleteProduct/${id}`, {
        method: 'DELETE',
        headers: { Authorization: `Bearer ${token}` }
      });

      if (res.ok) {
        toast.success('Product deleted successfully!');
        fetchProducts();
      } else {
        const data = await res.json();
        toast.error(data.message || 'Failed to delete product');
      }
    } catch (error) {
      console.error('Delete error:', error);
    }
  };

  const toggleActiveStatus = async (id) => {
    try {
      const res = await fetch(`http://it342-credigo-msd3.onrender.com/api/products/deactivate/${id}`, {
        method: 'PUT',
        headers: { Authorization: `Bearer ${token}` }
      });

      if (res.ok) {
        fetchProducts();
      } else {
        toast.error('Failed to update status');
      }
    } catch (error) {
      console.error('Status toggle error:', error);
    }
  };

  const handleEdit = (product) => {
    setEditProductId(product.productid);
    setEditForm({ ...product });
  };

  const handleEditChange = (e) => {
    const { name, value } = e.target;
    setEditForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleUpdateSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch(`http://it342-credigo-msd3.onrender.com/api/products/updateProduct/${editForm.productid}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`
        },
        body: JSON.stringify(editForm)
      });

      if (res.ok) {
        toast.success('Product updated successfully!');
        setEditProductId(null);
        fetchProducts();
      } else {
        const err = await res.text();
        toast.error(err || 'Failed to update product');
      }
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  return (
    <div className="p-8 bg-gray-100 min-h-screen max-w-7xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Manage Products</h1>

      {products.length === 0 && (
        <p className="text-center text-gray-500">No products found.</p>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {products.map((product) => (
          <div
            key={product.productid}
            className="bg-white border border-gray-300 p-4 rounded-lg shadow-md text-black min-h-[320px]"
          >
            <img
              src={product.imageUrl || 'https://via.placeholder.com/150'}
              alt={product.productname}
              className="w-full h-40 object-cover rounded mb-4"
            />

            {editProductId === product.productid ? (
              <form onSubmit={handleUpdateSubmit} className="space-y-2">
                <input name="productname" value={editForm.productname} onChange={handleEditChange} className="w-full border p-1" />
                <input name="description" value={editForm.description} onChange={handleEditChange} className="w-full border p-1" />
                <input name="price" type="number" value={editForm.price} onChange={handleEditChange} className="w-full border p-1" />
                <input name="salePrice" type="number" value={editForm.salePrice} onChange={handleEditChange} className="w-full border p-1" />
                <input name="imageUrl" value={editForm.imageUrl} onChange={handleEditChange} className="w-full border p-1" />
                <button type="submit" className="bg-blue-500 text-white px-3 py-1 rounded">Save</button>
                <button type="button" onClick={() => setEditProductId(null)} className="ml-2 text-gray-500">Cancel</button>
              </form>
            ) : (
              <>
                <h2 className="text-lg font-semibold">{product.productname}</h2>
                <p className="text-sm text-gray-600">{product.description}</p>
                <p className="mt-2 font-medium text-green-600">
                  {product.salePrice ? (
                    <>
                      <span className="line-through mr-2 text-red-500">${product.price}</span>
                      <span>${product.salePrice}</span>
                    </>
                  ) : (
                    `$${product.price}`
                  )}
                </p>
                <p className="text-xs mt-1">Category: {product.category?.categoryname || '—'}</p>
                <p className={`text-xs mt-1 ${product.isActive ? 'text-green-500' : 'text-red-500'}`}>
                  {product.isActive ? 'Active' : 'Inactive'}
                </p>
                <div className="mt-4 flex justify-between gap-2">
                  <button onClick={() => toggleActiveStatus(product.productid)} className="text-sm px-2 py-1 bg-yellow-400 text-white rounded">
                    {product.isActive ? 'Deactivate' : 'Activate'}
                  </button>
                  <button onClick={() => handleEdit(product)} className="text-sm px-2 py-1 bg-blue-500 text-white rounded">
                    Edit
                  </button>
                  <button onClick={() => handleDelete(product.productid)} className="text-sm px-2 py-1 bg-red-600 text-white rounded">
                    Delete
                  </button>
                </div>
              </>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default ManageProducts;
