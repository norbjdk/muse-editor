import heroImage from "../assets/images/hero.jpg";

const features = [
    {
        title: "Encourage 1",
        icon: "👻"
    },
    {
        title: "Encourage 2",
        icon: "👻"
    },
    {
        title: "Encourage 3",
        icon: "👻"
    },
    {
        title: "Encourage 4",
        icon: "👻"
    }
];

function Home() {
    return(
        <div className={`flex flex-col items-center justify-center w-full text-[#FFFCF2] gap-10`}>
            <div className={`mx-auto flex flex-col md:flex-row items-center gap-10 py-20`}>
                
                <div className={`flex-1 flex justify-center`}>
                    <img src={heroImage} alt="Hook Image" className={`w-full max-w-4xl rounded-2xl shadow-2xl`}/>
                </div>

                <div className={`flex-1 text-center md:text-right space-y-6 flex justify-end flex-col`}>
                    <h1 className={`text-[#2c2c2c] text-5xl md:text-6xl font-bold tracking-tight`}>Compose <span className="text-[#EB5E28]">without</span> limits.</h1>
                    <p className={`text-[#3b3b3b] text-lg md:text-xl max-w-2xl ml-auto`}>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam a purus ornare, interdum tellus sed, elementum massa. Aliquam tristique enim quis feugiat tincidunt. 
                    <span className={`font-semibold text-[#FFFCF2]`}></span> . </p>   
                    <div className={`flex justify-end flex-col md:flex-row gap-4 p-4`}>
                        <button className={`px-8 py-3 bg-[#EB5E28] text-[#252422] hover:bg-[#ff6d38] rounded-full font-sans font-bold transition-all transform hover:scale-105 cursor-pointer shadow-lg`}>
                            Start Composing
                        </button>
                        <button className={`px-8 py-3 border-2 border-[#CCC5B9] text-[#2c2c2c] hover:bg-[#CCC5B9] hover:text-[#252422] rounded-full font-sans font-semibold transition-all cursor-pointer`}>
                            Explore Scores
                        </button>
                    </div>
                </div>
            </div>

            <section className={`bg-[#E8E4DD] w-full py-12 px-8 rounded-2xl shadow-2xl`}>
                <div className={`min-w-9/12 max-w-full mx-auto`}>
                    <div className={`grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8`}>
                        {features.map((feature, index) => (
                            <div key={index} className={`flex items-center gap-4 bg-[#EB5E28] border cursor-pointer border-[#CCC5B9]/10 p-3 rounded-lg hover:border-[#EB5E28]/50 transition-all group hover:-translate-y-2`}>
                                <div className={`text-lg group-hover:scale-110 transition-transform duration-300`}>
                                    {feature.icon}
                                </div>
                                <h3 className={`text-[#FFFCF2] font-bold text-lg`}>
                                    {feature.title}
                                </h3>
                            </div>
                        ))}
                    </div>
                </div>
            </section>
        </div>
    );
}

export default Home;