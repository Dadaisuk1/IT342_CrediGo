import React, { Suspense, lazy } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import Dashboard from './pages/admin/Dashboard';

// Lazy load page components
// for users
const SignUp = lazy(() => import('./pages/SignUp'));
const SignIn = lazy(() => import('./pages/SignIn'));
const Home = lazy(() => import('./pages/Home'));
const Shop = lazy(() => import('./pages/Shop'));
const Transactions = lazy( () => import('./pages/Transactions'));
const Wishlist = lazy(() => import('./pages/Wishlist'));
const About = lazy(() => import('./pages/About'));
const NotFound = lazy(() => import('./pages/NotFound'));
const SignUpV2 = lazy(() => import('./pages/SignUpv2'));

// lazy load page for admin
const AdminDashboard = lazy(() => import('./pages/admin/AdminDashboard'));
/*
// Example for protected routes (optional)
const ProtectedAdminRoute = () => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser'));
  const isAdmin = currentUser && currentUser.role === 'Admin';

  return isAdmin ? <AdminDashboard /> : <Navigate to="/404" replace />;
};
*/

function App() {
  return (
    <Suspense fallback={<div className="text-center p-10 text-gray-600">Loading...</div>}>
      <Routes>
        <Route path="/" element={<Navigate to="/home" />} />
        <Route path="/sign-in" element={<SignIn />} />
        <Route path="/sign-up" element={<SignUp />} />
        <Route path="/home" element={<Home />} />
        <Route path="/shop" element={<Shop />} />
        <Route path="/transactions" element={<Transactions />} />
        <Route path="/wishlist" element={<Wishlist />} />
        <Route path="/about" element={<About />} />
        <Route path="/admin/*" element={<AdminDashboard />} />
        <Route path="/dashboard" element={<Dashboard />} />
        {/* Designing */}
        <Route path="/sign-upv2" element={<SignUpV2 />} />

        {/* <Route path="/admin" element={<ProtectedAdminRoute />} /> */}

        <Route path="/404" element={<NotFound />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Suspense>
  );
}

export default App;
