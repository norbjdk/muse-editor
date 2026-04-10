import {Outlet} from "react-router-dom";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";

function MainLayout() {
    return (
        <div className={`flex flex-col min-h-screen bg-[#F4F4F4] justify-between items-center gap-10`}>
            <Navbar />
            <div className={`container mt-10`}>
                <Outlet />
            </div>
            <Footer />
        </div>
    );
}

export default MainLayout;