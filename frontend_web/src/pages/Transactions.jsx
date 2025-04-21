import React, { lazy } from 'react'
const Navbar = lazy(() => import('../components/layout/Navbar'));

const Transactions = () => {
    return (
        <>
            <Navbar />
            <div className="flex flex-col items-center justify-center h-[80vh] gap-6">
                <p className="text-[32px] font-semibold text-white">Transactions</p>
            </div>
        </>
    )
}

export default Transactions;
