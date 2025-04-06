import React from 'react'
import { Routes, Route, Navigate } from 'react-router-dom'
import SignUp from './pages/SignUp';
import SignIn from './pages/SignIn';
import Home from './pages/Home';
import NotFound from './pages/NotFound';

/*
const ProtectedAdminRoute = () => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser'));
  const isAdmin = currentUser && currentUser.role === 'Admin';

  return isAdmin ? <AdminDashboard /> : <Navigate to="/404" replace />;
};
*/

// const hideRoutes = ['/sign-in', 'sign-up', '/admin'];


function App() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to ="/home" />} />
      <Route path="/sign-in" element={<SignIn />} />
      <Route path="/sign-up" element={<SignUp />} />
      <Route path='/home' element={<Home />} />

        
      {/* Protected Admin Route */}
      {/* <Route path="/admin" element={<ProtectedAdminRoute />} /> */}
        
      {/* 404 Page */}
      <Route path="/404" element={<NotFound />} />
        
      {/* Catch-all route */}
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
}

export default App
