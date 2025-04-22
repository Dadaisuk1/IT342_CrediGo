/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/pages/*.{js,jsx}",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        lato: ['"Funnel Sans"', "sans-serif"],
        "league-gothic": ['"League Gothic"', "sans-serif"],
      },
      colors: {
        // Define your color palette based on the hex codes used
        "primary-dark": "#001737", // Used for footer, card bg, gradient end
        "primary-medium": "#002C6A", // Used for main bg, section bg, gradient start
        "primary-light": "#00419D", // Used for feature/CTA section bg
        "primary-highlight": "#0056D0", // Used for feature titles
        "accent-pink": "#EEBBC3", // Used for button bg
        "accent-pink-hover": "#f0c8ce", // Used for button hover bg
        "text-light": "#ffffff", // Main text color
        "text-muted": "#E6E6E6", // Secondary text color
        "text-on-accent": "#232946",
      },
    },
  },
  plugins: [],
};
