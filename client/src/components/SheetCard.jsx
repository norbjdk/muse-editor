import { Link } from "react-router-dom";

function SheetCard({ score }) {
    return (
        <div className="bg-white/20 border border-[#CCC5B9]/90 rounded-xl p-4 hover:bg-white/40 transition-colors cursor-pointer group shadow-sm backdrop-blur-sm">
            <div className="relative pb-[100%] overflow-hidden bg-gray-100 dark:bg-gray-700">
                <img 
                    src={score.imageUrl} 
                    alt={`${score.title} - ${score.author}`}
                    className="absolute w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
                />
            </div>
            <h3 className="text-[#050505] font-semibold truncate text-sm md:text-base">{score.title}</h3>
            <p className="text-[#333231] text-xs mt-1">{score.author}</p>
            <div className="mt-3 flex items-center justify-between">
                <span className="text-[10px] uppercase tracking-widest text-[#5a320e] font-extrabold">
                    {score.category}
                </span>
                <span className="text-[#131212] text-[10px] italic">
                    {score.views} views
                </span>
            </div>
        </div>
    );
}

export default SheetCard;