import React from 'react';
import { Link } from 'react-router-dom';

const LandingPage = () => {
  return (
    <div className="flex flex-col items-center justify-center h-screen bg-[#f4f4f4]">
      <h1 className="text-4xl font-bold text-[#232946] mb-4">Welcome to CrediGo!</h1>
      <p className="text-gray-600 mb-8">You're now on the landing page 🎉</p>

      <div className="flex gap-4">
        <Link
          to="/sign-in"
          className="px-6 py-3 bg-[#232946] text-white rounded-md hover:bg-[#1a1a2e] transition"
        >
          Sign In
        </Link>

        <Link
          to="/sign-up"
          className="px-6 py-3 bg-[#F3C6CD] text-[#232946] rounded-md hover:bg-[#e79ba7] transition"
        >
          Sign Up
        </Link>
      </div>
    </div>
  );
};

export default LandingPage;
