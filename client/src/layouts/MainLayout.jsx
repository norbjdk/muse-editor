import { Outlet } from "react-router-dom";
import Navbar from "../components/Hero";
import Footer from "../components/Footer";
import Navigation from "../components/Navigation";

function MainLayout() {
    return (
        <div className={`flex flex-col min-h-screen justify-between items-center gap-10 bg-[#efe4db]`}>
            <Navbar />
            <div className="flex flex-row w-full items-start px-6"> 
                <div className="flex-1 pl-10">
                    <Outlet />
                </div>
            </div>
            <Footer />
        </div>
    );
}
                                                                                                                                                                                                                                             

export default MainLayout;
