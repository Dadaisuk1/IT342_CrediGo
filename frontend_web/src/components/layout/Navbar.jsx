import React from 'react';
import logo from '../../assets/images/credigo_icon.svg';
import { IoSearch } from 'react-icons/io5';
import { FaRegUserCircle } from 'react-icons/fa';
import { TbCurrencyPeso } from "react-icons/tb";
import { NavLink, useLocation, useNavigate } from 'react-router-dom';

const Navbar = () => {
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

                {/* Logo */}
                <div className='flex items-center justify-center gap-2'>
                    <img className='w-8 h-8' src={logo} alt="Credigo Logo" />
                    <NavLink to="/" className="font-league-gothic text-3xl font-bold text-white tracking-widest">CrediGo</NavLink>
                </div>

                {/* Links */}
                <ul className="hidden md:flex gap-6 font-medium">
                    <li><NavLink to="/home" className={navLinkClass}>Home</NavLink></li>
                    <li><NavLink to="/shop" className={navLinkClass}>Shop</NavLink></li>
                    <li><NavLink to="/transactions" className={navLinkClass}>Transactions</NavLink></li>
                    <li><NavLink to="/wishlist" className={navLinkClass}>Wishlist</NavLink></li>
                    <li><NavLink to="/about" className={navLinkClass}>About</NavLink></li>
                </ul>
            </div>

            {/* Right Side: Wallet, Get Started or Profile/Search */}
            <div className="flex items-center gap-6">
                {/* Wallet */}
                <div
                    onClick={() => handleNavigation('/wallet')}
                    className="flex items-center cursor-pointer">
                    <span className="font-semibold text-white">Wallet:</span>
                    <a className="ml-1 text-[#fff] flex items-center">
                        <span><TbCurrencyPeso size={20} className="mr-[-1px]" /></span>0.00</a> {/* Fixed the amount to 0.00 */}
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

export default Navbar;
