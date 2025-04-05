import React, { useState } from 'react';
import { useLocation, useNavigate} from 'react-router-dom'
import pattern_bg from '../assets/pattern-bg.svg';
import { IoMdArrowRoundBack } from "react-icons/io";
import { FaGithub } from 'react-icons/fa';

const SignUp = () => {

    const location = useLocation();
    const navigate = useNavigate();
    const isSignIn = location.pathname === '/sign-in';
    const isSignUp = location.pathname === '/sign-up';
    
    const [isAgree, setIsAgree] = useState(false);

    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div className="flex w-screen h-screen bg-[#fff]">
            {/* Left side with image */}
            <div className="w-[60%] h-full">
                <img src={pattern_bg} alt="60%-background" className="w-full h-full object-cover" />
            </div>

            {/* Right side content */}
            <div className="w-[40%] flex flex-col mt-10 mb-10">
                {/* Back to home */}
                <div
                    className="text-right w-full px-6 py-4 flex gap-1 items-center cursor-pointer"
                    onClick={() => handleNavigation('/')}
                >
                    <IoMdArrowRoundBack size={20} color="#232946" />
                    <p className="text-[#232946] text-[16.67px] cursor-pointer font-medium">Back to Home</p>
                </div>

                {/* Breadcrumbs */}
                <div className='flex justify-center'>
                    {/* Sign In */}
                    <p 
                        onClick={() => handleNavigation('/sign-in')}
                        className={`p-5 text-[24px] cursor-pointer ${isSignIn ? 'border-b-4 border-[#232946] font-medium' : 'border-b border-gray-300'}`}>Sign in</p>
                    {/* Sign Up */}
                    <p
                        onClick={() => handleNavigation('/sign-up')}
                        className={`p-5 text-[24px] cursor-pointer ${isSignUp ? 'border-b-4 border-[#232946] font-medium' : 'border-b border-gray-300'}`}>Sign up</p>
                </div>

                {/* Form Section */}
                <div className="flex justify-center items-center flex-grow">
                    <div className="bg-[#232946] h-[600px] w-[80%] max-w-[600px] max-h-[700px] p-6 rounded-lg shadow-lg">
                        <h1 className="text-[32px] font-bold text-center mb-6 text-[#FFFFFE]">Sign Up</h1>

                        <form className="flex flex-col gap-6">
                            <div className="flex flex-col gap-1">
                                <label className="text-[18px] text-white font-medium">Email</label>
                                <input
                                    type="email"
                                    className="p-4 border border-gray-300 rounded-md text-sm text-[15px]"
                                    required
                                />
                            </div>
                            <div className="flex flex-col gap-1">
                                <label className="text-[18px] text-white font-medium">Password</label>
                                <input
                                    type="password"
                                    placeholder="Password"
                                    className="p-4 border border-gray-300 rounded-md focus:outline-none text-[15px]"
                                    required
                                />
                            </div>
                            <div className="flex flex-col gap-1">
                                <label className="text-[18px] text-white font-medium">Re-enter Password</label>
                                <input
                                    type="password"
                                    placeholder="Re-enter Password"
                                    className="p-4 border border-gray-300 rounded-md focus:outline-none text-[15px]"
                                    required
                                />
                            </div>
                            
                            <div className="flex items-start-center gap-2 text-[12px] text-gray-300">
                                <input type="checkbox" checked={isAgree} onChange={() => setIsAgree(!isAgree)} />
                                <label>I agree to the Terms and Conditions</label>
                            </div>

                            <button
                                type="submit"
                                disabled={!isAgree}
                                className={`text-[15px] font-medium py-4 rounded-md transition ${
                                    isAgree ? 'bg-[#F3C6CD] text-[#232946] hover:bg-[#E7909E]' : 'bg-[#9FA0F8] text-gray-700 cursor-not-allowed'
                                }`}
                            >
                                Sign Up
                            </button>
                        </form>

                        <p className="text-center text-[12px] text-gray-300 mt-6">or sign up with</p>

                        <button className="flex items-center justify-center gap-2 border border-gray-300 py-4 w-full mt-2 rounded-md">
                            <FaGithub size={20} color='#FFFFFE' />
                            <span className='text-[#fffffe]'>Sign up with GitHub</span>
                        </button>

                        <div className="mt-6 text-center text-[12px] text-gray-300">
                            <p className="inline-block">You already have an account?</p>
                            <span 
                                onClick={() => handleNavigation('/sign-in')}
                                className="ml-2 text-[#ffffff] font-semibold cursor-pointer hover:underline">
                                Sign in here
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SignUp;
