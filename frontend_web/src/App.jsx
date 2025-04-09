import React, { Suspense, lazy } from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

// Lazy load page components
const SignUp = lazy(() => import('./pages/SignUp'));
const SignIn = lazy(() => import('./pages/SignIn'));
const Home = lazy(() => import('./pages/Home'));
const Shop = lazy(() => import('./pages/Shop'));
const NotFound = lazy(() => import('./pages/NotFound'));

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

        {/* <Route path="/admin" element={<ProtectedAdminRoute />} /> */}

        <Route path="/404" element={<NotFound />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Suspense>
  );
}

export default App;
