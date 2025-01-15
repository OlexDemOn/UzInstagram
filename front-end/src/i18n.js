import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import Backend from "i18next-http-backend";

i18n.use(Backend) // Load translation files
    .use(LanguageDetector) // Detect user language
    .use(initReactI18next) // Bind with React
    .init({
        fallbackLng: "en", // Default language
        debug: true,
        interpolation: {
            escapeValue: false, // React already escapes content
        },
        backend: {
            loadPath: "../public/locales/{{lng}}/translation.json", // Path to translation files
        },
    });

export default i18n;
