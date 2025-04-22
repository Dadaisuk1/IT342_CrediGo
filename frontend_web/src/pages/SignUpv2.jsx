import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import 'react-datepicker/dist/react-datepicker.css';
import DatePicker from 'react-datepicker';
import backgroundGames from '../assets/images/background.svg';
import { FaGithub, FaGoogle, FaEye, FaEyeSlash, FaGamepad, FaCreditCard, FaShieldAlt, FaHistory } from 'react-icons/fa';

const SignUpV2 = () => {
  // Form states
  const [email, setEmail] = useState('');
  const [username, setUsername] = useState('');
  const [dob, setDob] = useState(null);
  const [phone, setPhone] = useState('+63 ');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  // UI states
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [isAgree, setIsAgree] = useState(false);
  const [acceptNewsletters, setAcceptNewsletters] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  // Navigation
  const navigate = useNavigate();

  const handlePhoneChange = (e) => {
    let value = e.target.value;
    if (!value.startsWith('+63')) value = '+63 ';
    let digits = value.replace('+63', '').replace(/\D/g, '');
    if (digits.length > 10) digits = digits.slice(0, 10);

    let formatted = '+63 ';
    if (digits.length > 0) formatted += digits.slice(0, 3);
    if (digits.length >= 4) formatted += ' ' + digits.slice(3, 6);
    if (digits.length >= 7) formatted += ' ' + digits.slice(6);

    setPhone(formatted);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccessMessage('');

    // Basic validation
    if (!email || !username || !password || !confirmPassword) {
      setError('Please fill in all required fields');
      return;
    }

    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    if (!isAgree) {
      setError('You must agree to the Terms of Service and Privacy Policy');
      return;
    }

    if (password.length < 8) {
      setError('Password must be at least 8 characters long');
      return;
    }

    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setError('Please enter a valid email address');
      return;
    }

    setIsLoading(true);

    try {
      const res = await fetch("http://localhost:8080/api/users/createUser", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          email,
          username,
          phonenumber: phone,
          dateofbirth: dob ? dob.toISOString().split('T')[0] : '',
          password,
          status: "active",
          wallet: '0.00',
          role: "Customer"
        }),
      });

      const text = await res.text();
      let data;

      try {
        data = JSON.parse(text);
      } catch {
        data = { message: text || 'Something went wrong.' };
      }

      if (res.ok) {
        setSuccessMessage('Account created successfully!');
        setTimeout(() => {
          navigate('/sign-in');
        }, 2000);
      } else {
        setError(data.message || 'Sign up failed.');
      }
    } catch (err) {
      setError('Error connecting to server. Please try again later.');
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleNavigation = (path) => {
    navigate(path);
  };

  return (
    <div
      className="min-h-screen overflow-hidden bg-fixed bg-cover bg-center relative"
      style={{ backgroundImage: `url(${backgroundGames})` }}
    >
      <div className="absolute inset-0 bg-black bg-opacity-60" />

      <div className="relative z-10 flex items-center justify-between mx-8 md:mx-16 min-h-screen text-white">
        <div className="hidden md:block w-2/5 pr-8">
          <h1 className="text-4xl font-bold mb-6">CrediGo</h1>
          <h2 className="text-2xl font-semibold mb-8">Your universal wallet for gaming microtransactions</h2>

          <div className="space-y-6">
            <div className="flex items-start space-x-4">
              <div className="bg-[#eebbc3] p-3 rounded-full">
                <FaGamepad className="text-[#232946]" size={24} />
              </div>
              <div>
                <h3 className="font-bold text-xl mb-2">One Wallet for All Games</h3>
                <p className="text-gray-300">Connect your favorite games and platforms to make secure payments in seconds.</p>
              </div>
            </div>

            <div className="flex items-start space-x-4">
              <div className="bg-[#eebbc3] p-3 rounded-full">
                <FaShieldAlt className="text-[#232946]" size={24} />
              </div>
              <div>
                <h3 className="font-bold text-xl mb-2">Secure Transactions</h3>
                <p className="text-gray-300">Industry-leading security protocols to keep your payment details safe.</p>
              </div>
            </div>

            <div className="flex items-start space-x-4">
              <div className="bg-[#eebbc3] p-3 rounded-full">
                <FaHistory className="text-[#232946]" size={24} />
              </div>
              <div>
                <h3 className="font-bold text-xl mb-2">Track Your Spending</h3>
                <p className="text-gray-300">Monitor all your gaming purchases in one place with detailed transaction history.</p>
              </div>
            </div>

            <div className="flex items-start space-x-4">
              <div className="bg-[#eebbc3] p-3 rounded-full">
                <FaCreditCard className="text-[#232946]" size={24} />
              </div>
              <div>
                <h3 className="font-bold text-xl mb-2">Exclusive Deals</h3>
                <p className="text-gray-300">Access special discounts and promotions across multiple gaming platforms.</p>
              </div>
            </div>
          </div>

          <div className="mt-8 p-4 bg-[#2a325a] rounded-lg border border-[#eebbc3]">
            <p className="font-semibold text-white italic">
              "CrediGo has revolutionized how I manage my in-game purchases.
              No more payment hassles across different platforms!"
              <span className="block mt-2 text-[#eebbc3]">— Mark S., Pro Gamer</span>
            </p>
          </div>
        </div>

        {/* Form Container */}
        <div className="w-full md:w-3/5 max-w-lg mx-auto md:mx-0">
          <div className="w-full max-h-[95vh] overflow-hidden p-8 bg-[#232946] rounded-lg shadow-2xl border border-[#4a5294]">
            <h2 className="text-2xl font-bold text-center mb-4">Join CrediGo</h2>
            <p className="text-gray-300 text-center mb-6">Your universal gaming wallet awaits</p>

            {error && (
              <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4 text-sm">
                {error}
              </div>
            )}

            {successMessage && (
              <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4 text-sm">
                {successMessage}
              </div>
            )}

            <div className="flex items-center gap-5 mb-5">
              <button className="flex items-center justify-center gap-2 border border-gray-300 py-2 w-full rounded-md hover:bg-[#2a2f4a] transition">
                <FaGithub size={18} />
                <span>GitHub</span>
              </button>
              <button className="flex items-center justify-center gap-2 border border-gray-300 py-2 w-full rounded-md hover:bg-[#2a2f4a] transition">
                <FaGoogle size={18} />
                <span>Google</span>
              </button>
            </div>

            <div className="flex items-center gap-4 mb-5">
              <hr className="flex-grow border-t border-gray-400" />
              <span className="text-white text-xs">or</span>
              <hr className="flex-grow border-t border-gray-400" />
            </div>

            <form className="flex flex-col gap-4" onSubmit={handleSubmit}>
              <div className="flex flex-col gap-1">
                <label htmlFor="email">Email*</label>
                <input
                  type="email"
                  name="email"
                  id="emailAddress"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="p-2 rounded-sm bg-[#232946] border border-white text-white text-sm focus:border-[#eebbc3] focus:outline-none"
                  required
                />
              </div>

              <div className="flex flex-col gap-1">
                <label htmlFor="username">Username*</label>
                <input
                  type="text"
                  name="username"
                  id="username"
                  placeholder="Choose a gaming handle"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  className="p-2 rounded-sm bg-[#232946] border border-white text-white text-sm focus:border-[#eebbc3] focus:outline-none"
                  required
                />
              </div>

              <div className="flex gap-4">
                <div className="flex flex-col gap-1 w-full">
                  <label htmlFor="phoneNumber">Phone Number (Optional)</label>
                  <input
                    type="text"
                    id="phoneNumber"
                    value={phone}
                    onChange={handlePhoneChange}
                    maxLength={17}
                    className="p-2 rounded-sm bg-[#232946] border border-white text-white text-sm focus:border-[#eebbc3] focus:outline-none"
                  />
                </div>

                <div className="flex flex-col gap-1 w-full">
                  <label htmlFor="dateOfBirth">Date of Birth</label>
                  <DatePicker
                    id="dateOfBirth"
                    selected={dob}
                    onChange={(date) => setDob(date)}
                    placeholderText="MM|DD|YY"
                    className="p-2 rounded-sm bg-[#232946] border border-white text-white text-sm w-full focus:border-[#eebbc3] focus:outline-none"
                    calendarClassName="custom-datepicker"
                    popperClassName="z-50"
                    maxDate={new Date()}
                    showYearDropdown
                    dropdownMode="select"
                    withPortal
                  />
                </div>
              </div>

              <div className="flex flex-col gap-1">
                <label>Password*</label>
                <div className="relative">
                  <input
                    type={showPassword ? 'text' : 'password'}
                    placeholder="Create a secure password"
                    className="p-2 pr-10 border border-white rounded-sm bg-[#232946] text-white text-sm w-full focus:border-[#eebbc3] focus:outline-none"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                  <span
                    className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer"
                    onClick={() => setShowPassword(!showPassword)}
                  >
                    {showPassword ? <FaEyeSlash size={18} /> : <FaEye size={18} />}
                  </span>
                </div>
                <p className="text-xs text-gray-300 mt-1">Minimum 8 characters with letters and numbers</p>
              </div>

              <div className="flex flex-col gap-1">
                <label>Confirm Password*</label>
                <div className="relative">
                  <input
                    type={showConfirmPassword ? 'text' : 'password'}
                    placeholder="Confirm your password"
                    className="p-2 pr-10 border border-white rounded-sm bg-[#232946] text-white text-sm w-full focus:border-[#eebbc3] focus:outline-none"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                  />
                  <span
                    className="absolute top-1/2 right-3 -translate-y-1/2 cursor-pointer"
                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                  >
                    {showConfirmPassword ? <FaEyeSlash size={18} /> : <FaEye size={18} />}
                  </span>
                </div>
              </div>

              <div className="flex items-start mt-2">
                <div className="flex items-center h-5">
                  <input
                    id="terms"
                    type="checkbox"
                    checked={isAgree}
                    onChange={() => setIsAgree(!isAgree)}
                    className="w-4 h-4 border border-gray-300 rounded bg-[#232946] focus:ring-3 focus:ring-[#eebbc3]"
                    required
                  />
                </div>
                <label htmlFor="terms" className="ml-2 text-sm text-gray-300">
                  I agree to the <span className="text-[#eebbc3] hover:underline cursor-pointer">Terms of Service</span> and <span className="text-[#eebbc3] hover:underline cursor-pointer">Privacy Policy</span>
                </label>
              </div>

              <button
                type="submit"
                disabled={!isAgree || isLoading}
                className={`font-semibold py-3 rounded-md transition mt-4 ${
                  isAgree && !isLoading
                    ? 'bg-[#eebbc3] text-[#232946] hover:bg-[#ffd5dc] cursor-pointer'
                    : 'bg-gray-500 text-gray-300 cursor-not-allowed'
                }`}
              >
                {isLoading ? 'Creating Account...' : 'Create Your Gaming Wallet'}
              </button>

              <div className="bg-[#2a325a] p-3 rounded-md mt-2">
                <p className="text-sm text-center text-white">
                  <span className="font-semibold">Bonus:</span> Get 500 CrediGo credits when you sign up today!
                </p>
              </div>

              <p className="text-sm text-gray-300 mt-4 text-center">
                Already have an account?
                <span
                  onClick={() => handleNavigation('/sign-in')}
                  className="ml-1 text-[#eebbc3] font-semibold cursor-pointer hover:underline"
                >
                  Sign in here
                </span>
              </p>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default SignUpV2;
