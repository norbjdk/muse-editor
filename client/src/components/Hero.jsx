import Background from "../assets/bg.png";

function Hero() {
  return (
    <div 
      className="bg-cover bg-center flex flex-col items-center justify-center gap-10 w-full px-6 py-12 min-h-48 text-center"
      style={{ backgroundImage: `url(${Background})` }} 
    >
      <div className="max-w-2xl flex flex-col gap-4">        
        <h1 className="text-6xl md:text-7xl font-extrabold tracking-tight text-[#E8E4DD] drop-shadow-sm text-shadow-slate-700 text-shadow-md">
          MUSE
        </h1>

        <p className="text-base md:text-2xl text-white/80 font-thin drop-shadow-sm drop-shadow-slate-500">
          Simple music notation software, that allows single artists or bands to create their own music sheets for desired instruments.
        </p>
      </div>
      <div className="flex flex-col sm:flex-row items-center gap-4 w-full justify-center">
        <button className="w-full sm:w-auto px-6 py-3 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm">
        View on GitHub
        </button>
        <button className="w-full sm:w-auto px-6 py-3 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm">
         Try it out
        </button>
        <button className="w-full sm:w-auto px-6 py-3 bg-white/50 hover:bg-white/70 text-slate-800 font-medium rounded-xl border border-stone-900/10 shadow-sm hover:shadow-md active:scale-[0.98] transition-all duration-200 cursor-pointer backdrop-blur-sm">
          Read Docs
        </button>
      </div>
    </div>
  );
}

export default Hero;
