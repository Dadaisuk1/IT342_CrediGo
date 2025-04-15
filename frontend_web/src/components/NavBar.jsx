import React from 'react';
import { IoSearch } from 'react-icons/io5';
import { FaRegUserCircle } from 'react-icons/fa';
import { NavLink, useLocation, useNavigate } from 'react-router-dom';

const NavBar = () => {
    const location = useLocation();
    const navigate = useNavigate();
    const isLoggedIn = false;

    const handleNavigation = (path) => {
        navigate(path);
    };

    const navLinkClass = ({ isActive }) =>
        isActive
            ? 'text-[#fffffe] underline underline-offset-8 font-semibold transition ease-in-out'
            : 'text-[#B8C1EC] hover:text-[#fffffe] transition ease-in-out';

    return (
        <nav className="flex items-center justify-between px-6 py-4 shadow-md">
            {/* Logo and Navigation Links */}
            <div className="flex items-center gap-8">
                <NavLink to="/" className="text-2xl font-bold text-blue-400">CrediGo</NavLink>
                <ul className="hidden md:flex gap-6 font-medium">
                    <li><NavLink to="/home" className={navLinkClass}>Home</NavLink></li>
                    <li><NavLink to="/shop" className={navLinkClass}>Shop</NavLink></li>
                    <li><NavLink to="/transactions" className={navLinkClass}>Transactions</NavLink></li>
                    <li><NavLink to="/wishlist" className={navLinkClass}>Wishlist</NavLink></li>
                    <li><NavLink to="/about" className={navLinkClass}>About</NavLink></li>
                    <li><NavLink to="/sign-upv2" className={navLinkClass}>Sign Upv2</NavLink></li>
                </ul>
            </div>

            {/* Right Side: Wallet, Get Started or Profile/Search */}
            <div className="flex items-center gap-6">
                {/* Wallet */}
                <div className="items-center gap-5">
                    <span className="font-semibold">Wallet:</span>
                    <a href="#" className="ml-1 text-[#EEBBC3]">(₱0.00)</a>
                </div>

                {/* Not Logged In */}
                {!isLoggedIn && (
                <a
                    onClick={() => handleNavigation('/sign-in')}
                    className="px-4 py-2 bg-[#EEBBC3] text-[#232946] font-bold rounded hover:bg-[#f0c8ce] transition cursor-pointer"
                >
                    Get Started
                </a>
                )}

                {/* Logged In: Search & Profile */}
                {isLoggedIn && (
                    <div className="flex items-center gap-4">
                    {/* Search with Icon Inside */}
                    <div className="relative">
                        <input
                        type="text"
                        placeholder="Search"
                        className="bg-[#fffffe] text-[#232946] w-[300px] h-10 pl-4 pr-10 rounded-md focus:outline-none"
                        />
                        <IoSearch className="absolute right-3 top-1/2 transform -translate-y-1/2 text-xl text-gray-600 cursor-pointer" />
                    </div>

                    {/* Profile Icon */}
                    <FaRegUserCircle className="text-2xl text-gray-600 cursor-pointer" />
                    </div>
                )}
            </div>
        </nav>
    );
};

export default NavBar;
