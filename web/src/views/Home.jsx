import heroImage from "../assets/images/hero.jpg";
import RecommendCard from "../components/RecommendCard";

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

const recommendedData = [
    { id: 1, title: "Nocturne Op. 9 No. 2", author: "Frédéric Chopin", imageUrl: "https://encrypted-tbn3.gstatic.com/shopping?q=tbn:ANd9GcSPWHImQJ8sSGLC7bjRMQxXYs21yOoVa88FxYZywEO8_r1H9XSIDYhLBgLSw4q8x9VD3nd6eZikYhub-d3uU2W6sb4reXtd&usqp=CAc", category: "Public Domain", views: "12k" },
    { id: 2, title: "Waltz Cis-Moll", author: "Norbert Dominkiewicz", imageUrl: "https://encrypted-tbn2.gstatic.com/shopping?q=tbn:ANd9GcTBaDZhsH2MYK6FGRn_nqG3L230tXHPXBUHKCYlebe0h4uaT5u8PAhZ92gwGmznmat0eVZCHtT7hg8OFjGSeO09gvPEwXpvF9TrTQE8qZVcB4ZrRw8-F0iG5w&usqp=CAc", category: "Original", views: "850" },
    { id: 3, title: "Moonlight Sonata", author: "L. van Beethoven", imageUrl: "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcRKpem5RN8Z5iKS7Z_sRy0UvqcX5dpupFHPpvVAV5xrNJ_07nPcDzW_0yDjE4ABoOz-zFTXttJaie56doTzbFSHKU47whdpWQ&usqp=CAc", category: "Public Domain", views: "25k" },
    { id: 4, title: "Jazz Improvisation #1", author: "Jan Kowalski", imageUrl: "https://encrypted-tbn0.gstatic.com/shopping?q=tbn:ANd9GcSHvRJvMp5kD9ZKfXaHeyBZhaeD-_3LmE0SlAXXE3t0i1Hrlk-OoXVUEphBURf_aQAud60qkf9sTaUTtGawyoWM2lpjHnre7fQkhP-amhOKSa7nAZ7TBS0k&usqp=CAc", category: "Original", views: "1.2k" },
    { id: 5, title: "Clair de Lune", author: "Debussy", imageUrl: "https://encrypted-tbn1.gstatic.com/shopping?q=tbn:ANd9GcRHhY0QB_0_Grmf3DfLVHgrb1itjTBH6DwiyS-ZtzzFQcAB33aat-6_JT6m9lCGke4tdSmddprwZJZDK0jwjDa3LoA_MFcbmg&usqp=CAc", category: "Public Domain", views: "99.2k" },
];

function Home() {
    return(
        <div className={`flex flex-col items-center justify-center w-full text-[#FFFCF2] gap-10`}>
            <div className={`mx-auto flex flex-col md:flex-row items-center gap-10 py-20`}>
                
                <div className={`flex-1 flex justify-center`}>
                    <img src={heroImage} alt="Hook Image" className={`w-full max-w-4xl rounded-2xl shadow-2xl`}/>
                </div>

                <div className={`flex-1 text-center md:text-right space-y-6 flex justify-end flex-col`}>
                    <h1 className={`text-[#2c2c2c] text-5xl md:text-6xl font-bold tracking-tight`}>Compose <span className="text-[#365603]">without</span> limits.</h1>
                    <p className={`text-[#3b3b3b] text-lg md:text-xl max-w-2xl ml-auto`}>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam a purus ornare, interdum tellus sed, elementum massa. Aliquam tristique enim quis feugiat tincidunt. 
                    <span className={`font-semibold text-[#FFFCF2]`}></span> . </p>   
                    <div className={`flex justify-end flex-col md:flex-row gap-4 p-4`}>
                        <button className={`px-8 py-3 bg-[#365603] text-[#ebe7e0] hover:bg-[#202e09] rounded-full font-sans font-bold transition-all transform hover:scale-105 cursor-pointer shadow-lg`}>
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
                            <div key={index} className={`flex items-center gap-4 bg-[#365603] border cursor-pointer border-[#CCC5B9]/10 p-3 rounded-lg hover:border-[#365603]/50 transition-all group hover:-translate-y-2`}>
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

            <section className="bg-transparent w-full rounded-2xl py-8 px-8">
                <div className="max-w-auto mx-auto">
                    <div className="flex justify-start items-end mb-8 border-b-3 border-[#252422] pb-4">
                        <div>
                            <h2 className="text-[#2c2c2c] text-3xl font-bold">Our recommendations</h2>
                        </div>
                    </div>

                    <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-6">
                        {recommendedData.map(score => (
                            <RecommendCard key={score.id} score={score} />
                        ))}
                    </div>
                </div>
            </section>
        </div>
    );
}

export default Home;