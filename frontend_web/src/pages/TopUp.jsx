import React, { useState } from 'react';
import toast from 'react-hot-toast';

const TopUp = () => {
  const [selectedAmount, setSelectedAmount] = useState(null);
  const [customAmount, setCustomAmount] = useState('');
  const [paymentMethod, setPaymentMethod] = useState('predefined');
  const token = localStorage.getItem('token');

  const predefinedAmounts = [5, 10, 20, 50, 100];

  const handleConfirm = async () => {
    const amount = paymentMethod === 'predefined' ? selectedAmount : customAmount;
    if (!amount || amount <= 0) {
      toast.error('Please select a valid amount!');
      return;
    }

    try {
      const res = await fetch(`https://it342-credigo-msd3.onrender.com/api/users/topup?amount=${amount}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (res.ok) {
        toast.success('Top-up successful!');
        setSelectedAmount(null);
        setCustomAmount('');
      } else {
        toast.error('Top-up failed! Please try again.');
      }
    } catch (err) {
      console.error('Top-up error:', err);
      toast.error('An error occurred. Please try again later.');
    }
  };

  return (
    <div className="min-h-screen bg-gray-50 flex items-center justify-center px-4 py-12">
      <div className="w-full max-w-lg bg-white shadow-lg rounded-lg p-6">
        <h1 className="text-2xl font-bold text-center text-[#232946] mb-6">Top Up Your Wallet</h1>

        {/* Payment Method Switch */}
        <div className="flex justify-center gap-6 mb-6">
          <label className="flex items-center gap-2">
            <input
              type="radio"
              value="predefined"
              checked={paymentMethod === 'predefined'}
              onChange={() => setPaymentMethod('predefined')}
            />
            <span className="text-sm text-gray-700">Select Amount</span>
          </label>
          <label className="flex items-center gap-2">
            <input
              type="radio"
              value="creditcard"
              checked={paymentMethod === 'creditcard'}
              onChange={() => setPaymentMethod('creditcard')}
            />
            <span className="text-sm text-gray-700">Credit Card</span>
          </label>
        </div>

        {/* Predefined Amount Buttons */}
        {paymentMethod === 'predefined' && (
          <div className="grid grid-cols-3 gap-4 mb-6">
            {predefinedAmounts.map((amount) => (
              <button
                key={amount}
                type="button"
                className={`border rounded-md py-3 text-center font-medium ${
                  selectedAmount === amount
                    ? 'bg-green-500 text-white border-green-600'
                    : 'bg-white text-gray-800 hover:bg-green-100'
                }`}
                onClick={() => setSelectedAmount(amount)}
              >
                ${amount}
              </button>
            ))}
          </div>
        )}

        {/* Custom Amount Field */}
        {paymentMethod === 'creditcard' && (
          <div className="mb-6">
            <label className="block text-sm font-medium text-gray-700 mb-2">Enter Amount ($)</label>
            <input
              type="number"
              value={customAmount}
              onChange={(e) => setCustomAmount(e.target.value)}
              placeholder="e.g., 25"
              className="w-full p-3 border rounded-md focus:outline-none"
              min="1"
              required
            />
            {/* Card field (for future real payment API integration) */}
            <div className="mt-4">
              <label className="block text-sm font-medium text-gray-700 mb-2">Card Number</label>
              <input
                type="text"
                placeholder="1234 5678 9012 3456"
                className="w-full p-3 border rounded-md focus:outline-none"
              />
            </div>
          </div>
        )}

        {/* Confirm Button */}
        <button
          onClick={handleConfirm}
          className="w-full bg-green-600 hover:bg-green-700 text-white py-3 rounded-md font-semibold transition"
        >
          Confirm Top-Up
        </button>
      </div>
    </div>
  );
};

export default TopUp;
