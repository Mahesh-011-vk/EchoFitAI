import React from 'react'
import {
  Flame,
  LayoutDashboard,
  Dumbbell,
  Apple,
  TrendingUp,
  MessageSquare,
  Smartphone,
  Monitor,
  Download,
  Code2
} from 'lucide-react'
import type { UserProfile } from '../types/fitness'

interface NavbarProps {
  activeTab: string
  setActiveTab: (tab: any) => void
  userProfile: UserProfile
  streakCount: number
  viewMode: 'desktop' | 'mobile'
  setViewMode: (mode: 'desktop' | 'mobile') => void
  onOpenAccount: () => void
}

export const Navbar: React.FC<NavbarProps> = ({
  activeTab,
  setActiveTab,
  userProfile,
  streakCount,
  viewMode,
  setViewMode,
  onOpenAccount
}) => {
  const navTabs = [
    { id: 'DASHBOARD', label: 'Dashboard', icon: LayoutDashboard },
    { id: 'WORKOUTS', label: 'Workouts', icon: Dumbbell },
    { id: 'DIET', label: 'Diet & Macros', icon: Apple },
    { id: 'PROGRESS', label: 'Progress', icon: TrendingUp },
    { id: 'COACH', label: 'Echo AI', icon: MessageSquare }
  ]

  return (
    <header className="sticky top-0 z-40 w-full backdrop-blur-xl bg-[#F8F5EE]/90 border-b border-[#E8E1D5] transition-all">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-[76px] flex items-center justify-between gap-4">
        
        {/* Brand & Logo with Classic Elegance */}
        <div id="nav-brand" className="flex items-center gap-3 cursor-pointer group" onClick={() => setActiveTab('DASHBOARD')}>
          <div className="w-10 h-10 rounded-2xl bg-gradient-to-br from-[#721C28] via-[#8C2434] to-[#4E101B] flex items-center justify-center text-white font-serif font-black text-xl shadow-lg shadow-[#721C28]/25 group-hover:scale-105 transition-transform">
            ✦
          </div>
          <div>
            <div className="flex items-center gap-2">
              <span className="font-serif font-black text-2xl tracking-wide text-[#231815]">
                ECHOFIT
              </span>
              <span className="text-[10px] bg-[#721C28]/10 border border-[#721C28]/25 text-[#721C28] px-2 py-0.5 rounded-full font-bold uppercase tracking-wider">
                AI 2.0
              </span>
            </div>
            <p className="text-[10px] text-[#7A6E6A] font-medium hidden sm:block tracking-wide">
              Intelligent Fitness & Routine Suite
            </p>
          </div>
        </div>

        {/* Central Nav Tabs (Desktop) - LIQUID FROSTED GLASS CAPSULE MATCHING IMAGE */}
        <nav className="hidden md:flex items-center p-1.5 liquid-glass-capsule shadow-lg">
          {navTabs.map(tab => {
            const Icon = tab.icon
            const isActive = activeTab === tab.id
            return (
              <button
                key={tab.id}
                id={`nav-${tab.id.toLowerCase()}`}
                onClick={() => setActiveTab(tab.id)}
                className={`relative z-10 flex items-center gap-2 px-4 py-2 rounded-full text-xs transition-all cursor-pointer ${
                  isActive
                    ? 'liquid-tab-active shadow-md text-[#721C28]'
                    : 'liquid-tab-idle font-medium'
                }`}
              >
                <Icon className={`w-3.5 h-3.5 ${isActive ? 'text-[#721C28]' : 'text-[#6A5D60]'}`} />
                <span>{tab.label}</span>
              </button>
            )
          })}
        </nav>

        {/* Right Action Tools */}
        <div className="flex items-center gap-2.5 sm:gap-3">
          
          {/* View Mode Toggle (Full Web App vs Mobile Companion) */}
          <button
            id="nav-view-mode-toggle"
            onClick={() => setViewMode(viewMode === 'desktop' ? 'mobile' : 'desktop')}
            title={viewMode === 'desktop' ? 'Switch to Mobile Emulator View' : 'Switch to Full Web App View'}
            className="flex items-center gap-1.5 bg-white/80 hover:bg-white border border-[#E6DDD0] text-[#3D3130] hover:text-[#721C28] px-3.5 py-2 rounded-full text-xs font-semibold cursor-pointer transition-all shadow-sm"
          >
            {viewMode === 'desktop' ? (
              <>
                <Smartphone className="w-3.5 h-3.5 text-[#721C28]" />
                <span className="hidden lg:inline text-[11px] font-medium">Mobile View</span>
              </>
            ) : (
              <>
                <Monitor className="w-3.5 h-3.5 text-[#721C28]" />
                <span className="hidden lg:inline text-[11px] font-medium">Full Web View</span>
              </>
            )}
          </button>

          {/* Streak Indicator with Burgundy Theme */}
          <div id="nav-streak-badge" className="flex items-center gap-1.5 bg-[#721C28]/10 border border-[#721C28]/20 px-3.5 py-1.5 rounded-full text-[#721C28] font-black text-xs shadow-sm">
            <Flame className="w-4 h-4 fill-[#721C28] text-[#721C28] animate-pulse" />
            <span>{streakCount}d Streak</span>
          </div>

          {/* Account & Stored Routines Button */}
          <button
            id="nav-account-btn"
            onClick={onOpenAccount}
            className={`flex items-center gap-2 px-3.5 py-1.5 rounded-full text-xs font-bold transition-all border cursor-pointer shadow-sm ${
              activeTab === 'ACCOUNT'
                ? 'liquid-tab-active text-[#721C28]'
                : 'bg-white/80 hover:bg-white border-[#E6DDD0] text-[#231815]'
            }`}
          >
            <div className="w-6 h-6 rounded-full bg-gradient-to-tr from-[#721C28] to-[#962537] text-white flex items-center justify-center font-serif font-black text-xs shadow-sm">
              {userProfile.name ? userProfile.name[0].toUpperCase() : 'U'}
            </div>
            <div className="hidden sm:flex flex-col text-left">
              <span className="text-xs font-bold leading-tight text-[#231815]">{userProfile.name || 'Account'}</span>
              <span className="text-[9px] text-[#721C28] font-bold leading-none">Routines Hub</span>
            </div>
          </button>

          {/* APK Link button (hidden on extra small) */}
          <a
            href="https://github.com/Mahesh-011-vk/EchoFitAI/releases"
            target="_blank"
            rel="noreferrer"
            className="hidden xl:flex items-center gap-1.5 bg-white/80 hover:bg-white border border-[#E6DDD0] text-[#3D3130] hover:text-[#721C28] px-3.5 py-2 rounded-full text-xs font-semibold transition-all shadow-sm"
          >
            <Download className="w-3.5 h-3.5 text-[#721C28]" />
            <span>Get APK</span>
          </a>

          {/* GitHub Link */}
          <a
            href="https://github.com/Mahesh-011-vk/EchoFitAI"
            target="_blank"
            rel="noreferrer"
            className="text-[#6A5E5B] hover:text-[#721C28] p-2 rounded-full hover:bg-white/80 transition-all"
            title="View Android Source Code"
          >
            <Code2 className="w-4 h-4" />
          </a>
        </div>

      </div>

      {/* Mobile Secondary Sub-navigation Bar */}
      <div className="md:hidden flex items-center justify-around px-2 py-2 border-t border-[#E8E1D5] bg-[#FAF7F2]">
        <div className="w-full liquid-glass-capsule flex items-center justify-around p-1">
          {navTabs.map(tab => {
            const Icon = tab.icon
            const isActive = activeTab === tab.id
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`relative z-10 flex flex-col items-center gap-0.5 py-1 px-3 rounded-full text-[10px] cursor-pointer transition-all ${
                  isActive ? 'liquid-tab-active text-[#721C28] font-black' : 'liquid-tab-idle font-medium'
                }`}
              >
                <Icon className={`w-4 h-4 ${isActive ? 'text-[#721C28]' : 'text-[#6A5D60]'}`} />
                <span>{tab.label}</span>
              </button>
            )
          })}
        </div>
      </div>
    </header>
  )
}
