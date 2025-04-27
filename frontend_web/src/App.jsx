import React from 'react';
import { Routes, Route } from 'react-router-dom';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import Homepage from './pages/Homepage';
import LandingPage from './pages/LandingPage';
import Shop from './pages/Shop';
import TopUp from './pages/TopUp';
import WishList from './pages/WishList';
import Layout from './components/Layout';

import AdminDashboard from './admin/AdminDashboard';
import CreateProduct from './admin/CreateProduct';
import ManageProducts from './admin/ManageProducts';
import ProfilePage from './pages/ProfilePage';

function App() {
  return (
    <Routes>
      {/* Public Routes */}
      <Route path="/" element={<LandingPage />} />
      <Route path="/sign-in" element={<SignIn />} />
      <Route path="/sign-up" element={<SignUp />} />

      {/* Admin Routes */}
      <Route path="/admin" element={<AdminDashboard />} />
      <Route path="/admin/create-product" element={<CreateProduct />} />
      <Route path="/admin/manage-products" element={<ManageProducts />} />
      

      {/* Main App Routes Wrapped in Layout */}
      <Route element={<Layout />}>
        <Route path="/home" element={<Homepage />} />
        <Route path="/shop" element={<Shop />} />
        <Route path="/top-up" element={<TopUp />} />
        <Route path="/wishlist" element={<WishList />} />
        <Route path="/profile" element={<ProfilePage />} />
      </Route>
    </Routes>
  );
}

export default App;
