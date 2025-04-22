// src/components/layout/Button.jsx
import React from 'react';
import PropTypes from 'prop-types';
import { NavLink } from 'react-router-dom';

const Button = ({ to, children, className = '', ...props }) => {
  // Base styles - using theme colors
  const baseStyles = "bg-accent-pink text-text-on-accent font-bold py-3 px-6 rounded-full hover:bg-accent-pink-hover transition duration-200 ease-in-out inline-block";

  // If 'to' prop is provided, render a NavLink for internal navigation
  if (to) {
    return (
      <NavLink
        to={to}
        className={`${baseStyles} ${className}`}
        {...props} // Spread remaining props like 'onClick', 'aria-label', etc.
      >
        {children}
      </NavLink>
    );
  }

  // Otherwise, render a standard button
  return (
    <button
      type="button" // Default type, can be overridden via props
      className={`${baseStyles} ${className}`}
      {...props}
    >
      {children}
    </button>
  );
};

Button.propTypes = {
  to: PropTypes.string, // Optional route for NavLink
  children: PropTypes.node.isRequired, // Button text or elements
  className: PropTypes.string, // Allow adding extra classes
};

export default Button;
