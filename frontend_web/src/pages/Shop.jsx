import React from 'react'
import NavBar from '../components/NavBar';

const Shop = () => {
    return (
        <>
            <NavBar />
            <div className="flex flex-col items-center justify-center h-[80vh] gap-6">
                <p className="text-[32px] font-semibold text-white">Shop</p>
            </div>
        </>
    );
}

export default Shop;