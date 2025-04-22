import React, { lazy } from 'react'
import { TbCurrencyPeso } from "react-icons/tb";
const Navbar = lazy(() => import('../components/layout/Navbar'))

const Wallet = () => {
  return(
    <>
      <Navbar />
      <div className="flex justify-center items-center m-0 gap-5 h-[90vh] w-full">
        <p className="text-white text-3xl">Current Balance:</p>
        <p className="text-[#EEBBC3] flex gap-1 text-2xl items-center">
        <TbCurrencyPeso size={30} color='#fff' />
        00.0</p>
      </div>
    </>
  )
}

export default Wallet;
