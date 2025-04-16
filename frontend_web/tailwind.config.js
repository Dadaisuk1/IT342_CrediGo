/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        lato: ['Funnel Sans', 'sans-serif'],
      },
      colors: {
        col50: '#f2f5fc',
        col100: '#e2eaf7',
        col200: '#ccdaf1',
        col300: '#a9c2e7',
        col400: '#80a3da',
        col500: '#6285cf',
        col600: '#4e6bc2',
        col700: '#445ab1',
        col800: '#3c4b91',
        col900: '#344174',
        primaryCol: '#344174',
      }
    },
  },
  plugins: [],
}
