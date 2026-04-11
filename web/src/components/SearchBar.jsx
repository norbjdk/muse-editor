function SearchBar() {
    return (
        <div className="relative w-full group">
            <input 
                type="text" 
                className="
                block w-full border border-stone-950 rounded-full py-2 pl-4 pr-4 text-[#050505]
                 placeholder-[#96918f] text-sm focus:outline-none focus:bg-[#0C0C0C0C]
                  focus:border-[#69605c] focus:ring-1 focus:ring-[#686362] transition-all shadow-inner" 
                placeholder="Search sheet music..."
            />

            <div className="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none transition-colors group-focus-within:text-[#d6cac5]">
                <svg 
                    className="h-4 w-4 text-[#242222]" 
                    fill="none" 
                    viewBox="0 0 24 24" 
                    stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
            </div>
        </div>
    );
}

export default SearchBar;