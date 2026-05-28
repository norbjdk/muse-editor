import "./styles/global.css";
import { Routes, Route } from "react-router-dom";
import { AuthProvider } from "./contexts/AuthContext";
import PrivateRoute from "./components/PrivateRoute";
import PublicRoute from "./components/PublicRoute";
import MainLayout from "./layouts/MainLayout";

import Home from "./views/Home";
import Account from "./views/Account";

function App() {
    return (
        <AuthProvider>
            <Routes>
                <Route element={<MainLayout />}>
                    <Route path="/" element={<Home />} />
                    <Route path="/account" element={
                        <PublicRoute>
                            <Account />
                        </PublicRoute>
                    } />
                </Route>
            </Routes>
        </AuthProvider>
    );
}

export default App;