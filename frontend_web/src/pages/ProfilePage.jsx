import React, { useEffect, useState } from 'react';
import toast from 'react-hot-toast';

const ProfilePage = () => {
  const [wallet, setWallet] = useState(0);
  const [transactions, setTransactions] = useState([]);
  const [userInfo, setUserInfo] = useState({
    userid: '',
    username: '',
    email: '',
    phonenumber: '',
    password: '', // for changing password if user wants
  });

  const token = localStorage.getItem('token');

  const fetchWallet = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/users/wallet', {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      setWallet(data);
    } catch (err) {
      console.error('Failed to fetch wallet:', err);
    }
  };

  const fetchUserInfo = async () => {
    try {
      const decodedToken = JSON.parse(atob(token.split('.')[1])); // decode JWT
      const username = decodedToken.sub;

      const res = await fetch(`http://localhost:8080/api/users/username/${username}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      setUserInfo({
        userid: data.userid,
        username: data.username,
        email: data.email,
        phonenumber: data.phonenumber || '',
        password: '', // leave password empty initially
      });
    } catch (err) {
      console.error('Failed to fetch user info:', err);
    }
  };

  const fetchTransactions = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/transactions/user/me', {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      setTransactions(data);
    } catch (err) {
      console.error('Failed to fetch transactions:', err);
    }
  };

  const handleChange = (e) => {
    setUserInfo({ ...userInfo, [e.target.name]: e.target.value });
  };

  const handleSaveChanges = async () => {
    try {
      const { userid, ...updatedData } = userInfo;

      const res = await fetch(`http://localhost:8080/api/users/updateUser/${userid}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(updatedData),
      });

      if (res.ok) {
        toast.success('Profile updated successfully!');
        fetchUserInfo(); // reload info
      } else {
        toast.error('Failed to update profile!');
      }
    } catch (err) {
      console.error('Error updating profile:', err);
      toast.error('An error occurred while updating the profile!');
    }
  };

  useEffect(() => {
    fetchWallet();
    fetchUserInfo();
    fetchTransactions();
  }, []);

  return (
    <div className="p-8 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Profile Management</h1>

      {/* Wallet Balance */}
      <div className="mb-8">
        <h2 className="text-lg font-semibold mb-2">Wallet Balance</h2>
        <p className="text-green-600 text-2xl font-bold">${wallet}</p>
      </div>

      {/* Edit Profile Section */}
      <div className="mb-12">
        <h2 className="text-lg font-semibold mb-4">Edit Profile</h2>

        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium">Username</label>
            <input
              type="text"
              name="username"
              value={userInfo.username}
              onChange={handleChange}
              className="border p-2 w-full rounded"
            />
          </div>
          <div>
            <label className="block text-sm font-medium">Email</label>
            <input
              type="email"
              name="email"
              value={userInfo.email}
              onChange={handleChange}
              className="border p-2 w-full rounded"
            />
          </div>
          <div>
            <label className="block text-sm font-medium">Phone Number</label>
            <input
              type="text"
              name="phonenumber"
              value={userInfo.phonenumber}
              onChange={handleChange}
              className="border p-2 w-full rounded"
            />
          </div>
          <div>
            <label className="block text-sm font-medium">New Password (optional)</label>
            <input
              type="password"
              name="password"
              value={userInfo.password}
              onChange={handleChange}
              className="border p-2 w-full rounded"
            />
          </div>
          <button
            onClick={handleSaveChanges}
            className="mt-4 bg-green-600 hover:bg-green-700 text-white py-2 px-6 rounded font-semibold"
          >
            Save Changes
          </button>
        </div>
      </div>

      {/* Transaction History */}
      <div>
        <h2 className="text-lg font-semibold mb-4">Transaction History</h2>
        {transactions.length === 0 ? (
          <p>No transactions found.</p>
        ) : (
          <ul className="space-y-4">
            {transactions.map((tx) => (
              <li key={tx.transactionId} className="border p-4 rounded shadow-sm">
                <p><strong>Amount:</strong> ${tx.amount}</p>
                <p><strong>Status:</strong> {tx.status}</p>
                <p><strong>Payment Method:</strong> {tx.paymentMethod}</p>
                <p><strong>Date:</strong> {new Date(tx.createdDate).toLocaleString()}</p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default ProfilePage;
