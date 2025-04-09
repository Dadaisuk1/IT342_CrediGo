import React from 'react';

const products = [
  { title: 'Steam Wallet Code', category: 'Steam', price: '$50.00' },
  { title: 'Riot Points', category: 'Riot', price: '$20.00' },
  { title: 'Garena Shells', category: 'Garena', price: '$10.00' },
  { title: 'Xbox Gift Card', category: 'Xbox', price: '$30.00' },
  { title: 'Mobile Legends Diamonds', category: 'ML', price: '$25.00' },
];

const Shop = () => {
  return (
    <div className="min-h-screen bg-gray-50 py-12 px-4 md:px-8">
      <h1 className="text-3xl font-bold text-center text-[#232946] mb-12">Shop Game Credits</h1>

      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-8">
        {products.map((product, index) => (
          <div key={index} className="bg-white rounded shadow p-4 flex flex-col justify-between">
            <div>
              <h3 className="text-sm font-medium text-gray-500">{product.category}</h3>
              <div className="h-40 bg-gray-200 rounded my-3 flex items-center justify-center">
                <span className="text-gray-500 text-sm">[Image Placeholder]</span>
              </div>
              <h2 className="font-semibold text-lg text-[#232946]">{product.title}</h2>
              <div className="text-yellow-400 text-sm mt-1">★★★★☆</div>
            </div>
            <div className="flex justify-between items-center mt-4">
              <span className="text-green-600 font-bold">{product.price}</span>
              <button className="bg-green-600 hover:bg-green-700 text-white px-3 py-1 rounded text-sm">
                Add to Cart
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Shop;
