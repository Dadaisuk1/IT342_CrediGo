import React from 'react';
import pattern_bg from '../assets/pattern-bg.svg';
import { IoMdArrowRoundBack } from "react-icons/io";
import { FaGithub } from 'react-icons/fa';

const Login = () => {
    return (
        <div className="flex w-screen h-screen bg-[#fff]">
            {/* Left side with image */}
            <div className="w-[60%] h-full">
                <img src={pattern_bg} alt="60%-background" className="w-full h-full object-cover" />
            </div>

            {/* Right side content */}
            <div className="w-[40%] flex flex-col">
                {/* Back to home */}
                <div className="text-right w-full px-6 py-4 flex gap-5 items-center">
                    <IoMdArrowRoundBack size={20} color="#232946" />
                    <p className="text-[#232946] text-[16.67px] cursor-pointer font-medium">Back to Home</p>
                </div>

                {/* Form Section */}
                <div className="flex justify-center items-center flex-grow bg-[#232946]">
                    <div className="bg-white w-[80%] max-w-[400px] p-6 rounded-lg shadow-lg">
                        <h1 className="text-2xl font-bold text-center mb-6 text-[#232946]">Sign Up</h1>

                        <form className="flex flex-col gap-4">
                            <input
                                type="email"
                                placeholder="Email"
                                className="p-2 border border-gray-300 rounded-md focus:outline-none"
                                required
                            />
                            <input
                                type="password"
                                placeholder="Password"
                                className="p-2 border border-gray-300 rounded-md focus:outline-none"
                                required
                            />
                            <input
                                type="password"
                                placeholder="Re-enter Password"
                                className="p-2 border border-gray-300 rounded-md focus:outline-none"
                                required
                            />

                            <div className="flex items-start gap-2 text-sm text-gray-600">
                                <input type="checkbox" required />
                                <label>I agree to the Terms and Conditions</label>
                            </div>

                            <button
                                type="submit"
                                className="bg-[#232946] text-white py-2 rounded-md hover:bg-[#1e1e38] transition"
                            >
                                Sign Up
                            </button>
                        </form>

                        <p className="text-center text-sm text-gray-500 mt-6">or sign up with</p>

                        <button className="flex items-center justify-center gap-2 border border-gray-300 py-2 w-full mt-2 rounded-md hover:bg-gray-100 transition">
                            <FaGithub size={20} />
                            <span>Sign up with GitHub</span>
                        </button>

                        <div className="mt-6 text-center text-sm text-gray-600">
                            <p className="inline-block">You already have an account?</p>
                            <span className="ml-2 text-[#232946] font-semibold cursor-pointer hover:underline">
                                Sign in here
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Login;
