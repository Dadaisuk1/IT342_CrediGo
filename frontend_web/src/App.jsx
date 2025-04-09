import React from 'react';
import { Routes, Route } from 'react-router-dom';
import SignIn from './pages/SignIn';
import SignUp from './pages/SignUp';
import Homepage from './pages/Homepage';
import LandingPage from './pages/LandingPage';
import Shop from './pages/Shop';
import TopUp from './pages/TopUp';
import WishList from './pages/WishList';
import Layout from './components/Layout'; // make sure this file exists

function App() {
  return (
    <Routes>
      <Route path="/" element={<LandingPage />} />
      <Route path="/sign-in" element={<SignIn />} />
      <Route path="/sign-up" element={<SignUp />} />

      {/* ✅ Nested under Layout */}
      <Route element={<Layout />}>
        <Route path="/home" element={<Homepage />} />
        <Route path="/shop" element={<Shop />} />
        <Route path="/top-up" element={<TopUp />} />
        <Route path="/wishlist" element={<WishList />} />
      </Route>
    </Routes>
  );
}

export default App;
