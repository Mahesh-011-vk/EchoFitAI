import React from 'react'

interface Logo {
  id: string
  name: string
  src: string
  gradient: string // Tailwind bg-gradient classes or styles
}

const LOGOS: Logo[] = [
  {
    id: 'procure',
    name: 'Procure',
    src: 'https://cdn.svgl.app/directory/brand/procure.svg',
    gradient: 'from-[#3b82f6]/20 to-[#4f46e5]/20',
  },
  {
    id: 'shopify',
    name: 'Shopify',
    src: 'https://cdn.svgl.app/directory/brand/shopify.svg',
    gradient: 'from-[#facc15]/20 to-[#d97706]/20',
  },
  {
    id: 'blender',
    name: 'Blender',
    src: 'https://cdn.svgl.app/directory/brand/blender.svg',
    gradient: 'from-[#60a5fa]/20 to-[#2563eb]/20',
  },
  {
    id: 'figma',
    name: 'Figma',
    src: 'https://cdn.svgl.app/directory/brand/figma.svg',
    gradient: 'from-[#a855f7]/20 to-[#db2777]/20',
  },
  {
    id: 'spotify',
    name: 'Spotify',
    src: 'https://cdn.svgl.app/directory/brand/spotify.svg',
    gradient: 'from-[#ec4899]/20 to-[#e11d48]/20',
  },
  {
    id: 'lottielab',
    name: 'Lottielab',
    src: 'https://cdn.svgl.app/directory/brand/lottielab.svg',
    gradient: 'from-[#fde047]/20 to-[#22c55e]/20',
  },
  {
    id: 'google-cloud',
    name: 'Google Cloud',
    src: 'https://cdn.svgl.app/directory/brand/google-cloud.svg',
    gradient: 'from-[#38bdf8]/20 to-[#3b82f6]/20',
  },
  {
    id: 'bing',
    name: 'Bing',
    src: 'https://cdn.svgl.app/directory/brand/bing.svg',
    gradient: 'from-[#22d3ee]/20 to-[#0d9488]/20',
  },
]

// Render the list twice inline to ensure a seamless loop
const SCROLL_LOGOS = [...LOGOS, ...LOGOS]

export const MarqueeScroller: React.FC = () => {
  return (
    <div
      className="relative w-full overflow-hidden py-4 select-none"
      style={{
        maskImage: 'linear-gradient(to right, transparent, white 15%, white 85%, transparent)',
        WebkitMaskImage: 'linear-gradient(to right, transparent, white 15%, white 85%, transparent)',
      }}
    >
      <div className="flex animate-marquee gap-8">
        {SCROLL_LOGOS.map((logo, index) => (
          <div
            key={`${logo.id}-${index}`}
            className="group relative h-24 w-40 shrink-0 flex items-center justify-center rounded-full bg-white border border-slate-200/60 shadow-sm hover:border-slate-300 transition-all overflow-hidden"
          >
            {/* Hover Background Gradient Layer */}
            <div
              className={`absolute inset-0 bg-gradient-to-br ${logo.gradient} opacity-0 scale-150 group-hover:opacity-100 group-hover:scale-100 transition-all duration-500 ease-out pointer-events-none`}
            />

            {/* Logo Image */}
            <img
              src={logo.src}
              alt={logo.name}
              className="h-7 max-w-[65%] object-contain z-10 transition-all duration-300 group-hover:brightness-0 group-hover:invert"
            />
          </div>
        ))}
      </div>
    </div>
  )
}
