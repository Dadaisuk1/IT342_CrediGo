import React from 'react';
import { Link } from 'react-router-dom';

const AdminDashboard = () => {
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100 p-8">
      <h1 className="text-4xl font-bold mb-8 text-[#232946]">Admin Dashboard</h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
        <Link to="/admin/create-product" className="bg-green-500 hover:bg-green-600 text-white px-6 py-4 rounded shadow text-center text-lg font-semibold">
          ➕ Create Product
        </Link>
        <Link to="/admin/manage-products" className="bg-blue-500 hover:bg-blue-600 text-white px-6 py-4 rounded shadow text-center text-lg font-semibold">
          🛠️ Manage Products
        </Link>
      </div>
    </div>
  );
};

export default AdminDashboard;
