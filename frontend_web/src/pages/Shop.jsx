import React, { Suspense, lazy } from 'react'

const Navbar = lazy(() => import('../components/layout/Navbar'))

const Shop = () => {
    return (
        <Suspense fallback={<div className="text-center p-10 text-gray-600">Loading...</div>}>
            <Navbar />
            <div className="flex flex-col items-center justify-center h-[80vh] gap-6">
                <p className="text-[32px] font-semibold text-white">Welcome to the Shop</p>
            </div>
        </Suspense>
    );
}

export default Shop;
