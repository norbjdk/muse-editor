function RecommendCard({ score }) {
    return (
        <div className="bg-[#E8E4DD] border border-[#CCC5B9]/10 rounded-xl p-4 hover:bg-[#cac0b0] transition-colors cursor-pointer group">
            <div className="aspect-6/5 bg-[#365603]/20 rounded-lg mb-4 flex items-center justify-center overflow-hidden border border-[#CCC5B9]/5">
                {score.imageUrl && score.imageUrl.trim() !== "" ? (
                    <img src={score.imageUrl} alt={score.title} className="..." />
                ) : (
                    <span className="text-[#CCC5B9]/20 text-4xl">🎼</span>
                )}
            </div>
            <h3 className="text-[#050505] font-semibold truncate text-sm md:text-base">{score.title}</h3>
            <p className="text-[#333231] text-xs mt-1">{score.author}</p>
            <div className="mt-3 flex items-center justify-between">
                <span className="text-[10px] uppercase tracking-widest text-[#365603] font-extrabold">
                    {score.category}
                </span>
                <span className="text-[#131212] text-[10px] italic">
                    {score.views} views
                </span>
            </div>
        </div>
    );
}

export default RecommendCard;