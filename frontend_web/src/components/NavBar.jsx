import React from 'react';
import { IoSearch } from 'react-icons/io5';
import { FaRegUserCircle } from 'react-icons/fa';
import { useLocation, useNavigate } from 'react-router-dom';

const NavBar = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const isSignIn = location.pathname === '/sign-in';
    const isSignUp = location.pathname === '/sign-up';
    const isHome = location.pathname === '/home';
    const isLoggedIn = false;
    
    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <nav className="flex items-center justify-between px-6 py-4 bg-white shadow-md">
            {/* Logo and Navigation Links */}
            <div className="flex items-center gap-8">
                <a href="#" className="text-2xl font-bold text-blue-600">CrediGo</a>
                <ul className="hidden md:flex gap-6 text-gray-700 font-medium">
                <li><a href="#">Home</a></li>
                <li><a href="#">Shop</a></li>
                <li><a href="#">Transactions</a></li>
                <li><a href="#">Wishlist</a></li>
                <li><a href="#">About</a></li>
                </ul>
            </div>

            {/* Right Side: Wallet, Get Started or Profile/Search */}
            <div className="flex items-center gap-6">
                {/* Wallet */}
                <div className="text-gray-800">
                    <span className="font-semibold">Wallet:</span>
                    <a href="#" className="ml-1">(₱0.00)</a>
                </div>

                {/* Not Logged In */}
                {!isLoggedIn && (
                <a
                    onClick={() => handleNavigation('/sign-in')}
                    className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition cursor-pointer"
                >
                    Get Started
                </a>
                )}

                {/* Logged In: Search & Profile */}
                {isLoggedIn && (
                <>
                    <IoSearch className="text-2xl cursor-pointer text-gray-600" />
                    <FaRegUserCircle className="text-2xl text-gray-600 cursor-pointer" />
                </>
                )}
            </div>
        </nav>
    );
};

export default NavBar;
