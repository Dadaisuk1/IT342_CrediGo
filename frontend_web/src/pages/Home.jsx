import React, { useState } from 'react'
import { useLocation, useNavigate } from 'react-router-dom';

const Home = () => {

    const location = useLocation();
    const navigate = useNavigate();
    const isSignIn = location.pathname === '/sign-in';
    const isSignUp = location.pathname === '/sign-up';

    const handleNavigation = (path) => {
        navigate(path);
    };

    return (
        <div className='flex justify-center'>
            <p className='Flex text-[32px] text-[#FFFFFE]'>Welcome Home!</p>
            <button
                className='p-5 text-[32px] text-[#FFFFFE]'
                onClick={() => handleNavigation('/sign-in')}
            >Sign In</button>
            <button
                className='p-5 text-[32px] text-[#FFFFFE]'
                onClick={() => handleNavigation('/sign-up')}
            >Sign Up</button>
        </div>
    );
};

export default Home;