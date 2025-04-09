import React, { useState } from 'react';

const dummyWishlist = [
  {
    id: 1,
    name: 'Steam Wallet Code',
    category: 'Steam',
    price: 20,
  },
  {
    id: 2,
    name: 'Riot Points',
    category: 'Riot Games',
    price: 10,
  },
  {
    id: 3,
    name: 'Mobile Legends Diamonds',
    category: 'Mobile Legends',
    price: 15,
  },
];

const WishList = () => {
  const [wishlist, setWishlist] = useState(dummyWishlist);

  const removeFromWishlist = (id) => {
    setWishlist((prev) => prev.filter((item) => item.id !== id));
  };

  const addToCart = (item) => {
    // Simulate adding to cart
    alert(`Added "${item.name}" to cart!`);
  };

  return (
    <div className="min-h-screen bg-gray-50 px-4 py-10">
      <div className="max-w-4xl mx-auto bg-white shadow-md rounded-lg p-6">
        <h1 className="text-2xl font-bold text-[#232946] mb-6">Your Wishlist</h1>

        {wishlist.length === 0 ? (
          <p className="text-center text-gray-600">Your wishlist is empty. 💔</p>
        ) : (
          <div className="space-y-4">
            {wishlist.map((item) => (
              <div
                key={item.id}
                className="flex justify-between items-center border p-4 rounded-md shadow-sm"
              >
                <div>
                  <h2 className="font-semibold text-[#232946]">{item.name}</h2>
                  <p className="text-sm text-gray-500">{item.category}</p>
                  <p className="font-bold text-green-600">${item.price.toFixed(2)}</p>
                </div>
                <div className="flex gap-3">
                  <button
                    onClick={() => addToCart(item)}
                    className="bg-green-500 hover:bg-green-600 text-white text-sm px-3 py-2 rounded-md"
                  >
                    Add to Cart
                  </button>
                  <button
                    onClick={() => removeFromWishlist(item.id)}
                    className="bg-red-500 hover:bg-red-600 text-white text-sm px-3 py-2 rounded-md"
                  >
                    Remove
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default WishList;
