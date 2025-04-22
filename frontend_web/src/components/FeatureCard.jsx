// src/components/FeatureCard.jsx
import React from 'react';
import PropTypes from 'prop-types'; // Recommended for defining prop types

const FeatureCard = ({ title, description, icon }) => ( // Added optional icon prop
  // Using theme colors defined in tailwind.config.js
  <div className="bg-primary-dark rounded-lg p-6 shadow-md text-center md:text-left">
    {/* Optional: Add an icon area */}
    {icon && <div className="mb-4 text-accent-pink text-4xl mx-auto md:mx-0 w-fit">{icon}</div>}
    <h3 className="text-xl font-semibold mb-2 text-primary-highlight">{title}</h3>
    <p className="text-text-muted">{description}</p>
  </div>
);

// Define prop types for better component documentation and error checking
FeatureCard.propTypes = {
  title: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  icon: PropTypes.node, // Accepts any renderable node (e.g., an SVG component)
};

export default FeatureCard;
