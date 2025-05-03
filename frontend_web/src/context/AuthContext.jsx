import React, { createContext, useState, useEffect, useContext } from 'react';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [wallet, setWallet] = useState(0);
  const [mails, setMails] = useState([]);

  const token = localStorage.getItem('token');

  const login = (token) => {
    localStorage.setItem('token', token);
    setIsAuthenticated(true);
    fetchWallet();
    fetchMails();
  };

  const logout = () => {
    localStorage.removeItem('token');
    setIsAuthenticated(false);
    setWallet(0);
    setMails([]);
  };

  const fetchWallet = async () => {
    if (!token) return;
    try {
      const res = await fetch('https://it342-credigo-msd3.onrender.com/api/users/wallet', {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      if (data.error) {
        throw new Error(data.error);
      }
      setWallet(data);
    } catch (error) {
      console.error('Failed to fetch wallet:', error);
      logout();  // Optionally log out if there's an error
    }
  };

  const fetchMails = async () => {
    if (!token) return;
    try {
      const res = await fetch('https://it342-credigo-msd3.onrender.com/api/mails/getUserMails/me', {
        headers: { Authorization: `Bearer ${token}` },
      });
      const data = await res.json();
      if (data.error) {
        throw new Error(data.error);
      }
      setMails(data);
    } catch (error) {
      console.error('Failed to fetch mails:', error);
      logout();  // Optionally log out if there's an error
    }
  };

  const refetchWalletAndMails = async () => {
    await fetchWallet();
    await fetchMails();
  };

  useEffect(() => {
    if (token) {
      setIsAuthenticated(true);
      fetchWallet();
      fetchMails();
    }
  }, [token]);

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout, wallet, mails, refetchWalletAndMails }}>
      {children}
    </AuthContext.Provider>
  );
};

// Create a hook for easier use
export const useAuth = () => useContext(AuthContext);
