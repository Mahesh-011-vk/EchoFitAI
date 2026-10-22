import React, { useState, useEffect } from 'react'
import { Play, Pause, X, CheckCircle2, Wind } from 'lucide-react'
import type { GuidedProgram } from '../types/fitness'

interface ProgramSessionModalProps {
  program: GuidedProgram | null
  onClose: () => void
  onCompleteProgram: (programId: string) => void
}

export const ProgramSessionModal: React.FC<ProgramSessionModalProps> = ({
  program,
  onClose,
  onCompleteProgram
}) => {
  if (!program) return null

  const totalSeconds = program.durationMinutes * 60
  const [secondsRemaining, setSecondsRemaining] = useState(totalSeconds)
  const [isPlaying, setIsPlaying] = useState(true)

  useEffect(() => {
    let interval: any
    if (isPlaying && secondsRemaining > 0) {
      interval = setInterval(() => {
        setSecondsRemaining(s => s - 1)
      }, 1000)
    } else if (secondsRemaining === 0 && isPlaying) {
      setIsPlaying(false)
      onCompleteProgram(program.id)
    }
    return () => clearInterval(interval)
  }, [isPlaying, secondsRemaining, program.id, onCompleteProgram])

  const progressPercent = Math.round(((totalSeconds - secondsRemaining) / totalSeconds) * 100)
  const currentStepIdx = Math.min(
    program.steps.length - 1,
    Math.floor(((totalSeconds - secondsRemaining) / totalSeconds) * program.steps.length)
  )

  return (
    <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-xl flex items-center justify-center p-4">
      <div className="bg-[#FAF7F2] border border-[#E5DDD0] rounded-[36px] max-w-lg w-full p-6 sm:p-8 shadow-2xl flex flex-col gap-6 text-left relative overflow-hidden">
        
        {/* Subtle Warm Aura in Background */}
        <div 
          className="absolute -top-16 -right-16 w-64 h-64 rounded-full blur-3xl opacity-25 pointer-events-none"
          style={{ backgroundColor: program.auraColor || '#721C28' }}
        />

        {/* Header */}
        <div className="flex justify-between items-start border-b border-[#E8E1D5] pb-4 relative z-10">
          <div>
            <div className="flex items-center gap-2 mb-1">
              <span className="text-[10px] font-black uppercase tracking-wider px-3 py-0.5 rounded-full bg-[#721C28]/10 text-[#721C28] border border-[#721C28]/25">
                Guided Program ● {program.completedSessions}/{program.totalSessions} Sessions
              </span>
            </div>
            <h2 className="font-serif font-black text-2xl text-[#231815] tracking-tight">
              {program.title}
            </h2>
            <p className="text-xs text-[#5C4F4D] font-medium mt-0.5">{program.subtitle}</p>
          </div>
          <button onClick={onClose} className="text-[#7A6E6B] hover:text-[#231815] p-2 rounded-full hover:bg-white cursor-pointer">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Dynamic Breathing / Aura Visualization Card */}
        <div className="w-full h-44 rounded-3xl bg-white border border-[#E5DDD0] flex flex-col items-center justify-center relative overflow-hidden group shadow-inner">
          <div 
            className="w-32 h-32 rounded-full absolute transition-all duration-1000 animate-pulse-glow"
            style={{
              background: `radial-gradient(circle, ${program.auraColor || '#721C28'} 0%, rgba(114, 28, 40, 0.2) 65%, transparent 100%)`,
              filter: 'blur(20px)'
            }}
          />

          <div className="relative z-10 flex flex-col items-center gap-2 text-center">
            <Wind className="w-8 h-8 text-[#721C28] animate-pulse" />
            <div className="font-mono text-3xl font-black text-[#231815] tracking-tight">
              {String(Math.floor(secondsRemaining / 60)).padStart(2, '0')}:{String(secondsRemaining % 60).padStart(2, '0')}
            </div>
            <span className="text-[11px] font-extrabold text-[#721C28] uppercase tracking-widest bg-[#FAF7F2] px-3.5 py-1 rounded-full border border-[#E5DDD0] shadow-sm">
              {program.steps[currentStepIdx]}
            </span>
          </div>
        </div>

        {/* Progress Bar */}
        <div className="flex flex-col gap-1.5">
          <div className="flex justify-between text-xs font-bold text-[#3D3130]">
            <span>Session Progress</span>
            <span className="text-[#721C28] font-extrabold">{progressPercent}%</span>
          </div>
          <div className="w-full bg-[#EFE9DF] h-2 rounded-full overflow-hidden border border-[#E5DDD0]">
            <div
              className="bg-gradient-to-r from-[#721C28] to-[#9E2A3C] h-full rounded-full transition-all duration-500"
              style={{ width: `${progressPercent}%` }}
            />
          </div>
        </div>

        {/* Steps Breakdown */}
        <div className="bg-white border border-[#E5DDD0] rounded-2xl p-4 flex flex-col gap-2 shadow-sm">
          <span className="text-[10px] font-black uppercase text-[#7A6E6B] tracking-wider">Protocol Sequence:</span>
          {program.steps.map((step, idx) => (
            <div
              key={idx}
              className={`flex items-center gap-2.5 text-xs transition-all ${
                idx === currentStepIdx
                  ? 'text-[#721C28] font-extrabold'
                  : idx < currentStepIdx
                  ? 'text-[#A59995] line-through'
                  : 'text-[#5C4F4D] font-medium'
              }`}
            >
              <span className={`w-4 h-4 rounded-full flex items-center justify-center text-[10px] ${
                idx < currentStepIdx
                  ? 'bg-emerald-600 text-white'
                  : idx === currentStepIdx
                  ? 'bg-[#721C28] text-white animate-pulse'
                  : 'bg-[#FAF7F2] text-[#7A6E6B] border border-[#E5DDD0]'
              }`}>
                {idx < currentStepIdx ? '✓' : idx + 1}
              </span>
              <span>{step}</span>
            </div>
          ))}
        </div>

        {/* Controls */}
        <div className="flex items-center gap-3 pt-2">
          <button
            onClick={() => setIsPlaying(!isPlaying)}
            className="flex-1 py-3 bg-white hover:bg-[#F3EFE6] text-[#231815] font-extrabold rounded-full text-xs flex items-center justify-center gap-2 border border-[#E5DDD0] transition-all cursor-pointer shadow-sm"
          >
            {isPlaying ? <Pause className="w-4 h-4" /> : <Play className="w-4 h-4 fill-[#231815]" />}
            <span>{isPlaying ? 'PAUSE' : 'RESUME'}</span>
          </button>

          <button
            onClick={() => onCompleteProgram(program.id)}
            className="flex-1 py-3 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white font-black rounded-full text-xs flex items-center justify-center gap-2 shadow-lg shadow-[#721C28]/20 uppercase tracking-wider transition-all cursor-pointer"
          >
            <CheckCircle2 className="w-4 h-4" />
            <span>MARK FINISHED</span>
          </button>
        </div>

      </div>
    </div>
  )
}
