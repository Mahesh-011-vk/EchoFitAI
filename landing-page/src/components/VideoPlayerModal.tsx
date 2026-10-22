import { useState, useEffect } from 'react'
import { Play, Pause, X, Flame, CheckCircle2 } from 'lucide-react'
import type { CardioSession } from '../types/fitness'

interface VideoPlayerModalProps {
  session: CardioSession | null
  onClose: () => void
  onComplete: () => void
}

export const VideoPlayerModal: React.FC<VideoPlayerModalProps> = ({
  session,
  onClose,
  onComplete
}) => {
  if (!session) return null

  const totalSeconds = session.durationMinutes * 60
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
      onComplete()
    }
    return () => clearInterval(interval)
  }, [isPlaying, secondsRemaining, onComplete])

  const progressPercent = Math.round(((totalSeconds - secondsRemaining) / totalSeconds) * 100)
  const currentStepIndex = Math.min(
    session.steps.length - 1,
    Math.floor(((totalSeconds - secondsRemaining) / totalSeconds) * session.steps.length)
  )

  return (
    <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-md flex items-center justify-center p-4">
      <div className="bg-[#FAF7F2] border border-[#E5DDD0] rounded-3xl max-w-lg w-full p-6 shadow-2xl flex flex-col gap-5 text-left">
        
        {/* Header */}
        <div className="flex justify-between items-center border-b border-[#E8E1D5] pb-3">
          <div>
            <div className="flex items-center gap-2">
              <span className="text-[10px] font-extrabold text-[#721C28] bg-[#721C28]/10 px-3 py-0.5 rounded-full uppercase border border-[#721C28]/25">
                {session.category} Studio
              </span>
              <span className="text-xs text-[#7A6E6B]">● {session.difficulty}</span>
            </div>
            <h3 className="font-serif font-black text-lg text-[#231815] mt-0.5">{session.name}</h3>
          </div>
          <button onClick={onClose} className="text-[#7A6E6B] hover:text-[#231815] p-1 rounded-full hover:bg-white">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Video Canvas Representation */}
        <div className="w-full h-48 rounded-2xl bg-black flex flex-col items-center justify-center relative overflow-hidden border border-[#E5DDD0] shadow-inner group">
          {/* Animated Background Pulse */}
          <div className="absolute inset-0 bg-gradient-to-br from-[#721C28]/20 via-black/40 to-transparent" />

          {isPlaying ? (
            <div className="flex flex-col items-center gap-2 z-10 text-center">
              <div className="flex items-center gap-2">
                <span className="w-3 h-3 bg-red-500 rounded-full animate-ping" />
                <span className="text-xs font-black tracking-widest text-white uppercase">
                  STREAMING STUDIO WORKOUT...
                </span>
              </div>
              <p className="text-xs text-[#E8D9C5] font-bold bg-black/70 px-3 py-1 rounded-full border border-white/10">
                Active: {session.steps[currentStepIndex]}
              </p>
            </div>
          ) : (
            <div
              className="flex flex-col items-center gap-2 z-10 cursor-pointer"
              onClick={() => setIsPlaying(true)}
            >
              <div className="w-12 h-12 rounded-full bg-[#721C28] text-white flex items-center justify-center shadow-lg shadow-[#721C28]/30">
                <Play className="w-6 h-6 fill-white ml-1" />
              </div>
              <span className="text-xs font-bold text-slate-200">Click to Resume Studio</span>
            </div>
          )}

          {/* Bottom Live Badges */}
          <div className="absolute bottom-3 left-3 right-3 flex justify-between items-center text-[10px] font-bold text-white z-10">
            <span className="bg-black/70 px-2.5 py-1 rounded-lg border border-white/10 flex items-center gap-1 text-[#E5B5BC]">
              <Flame className="w-3.5 h-3.5 fill-[#E5B5BC]" /> ~{session.caloriesBurned} kcal
            </span>
            <span className="bg-black/70 px-2.5 py-1 rounded-lg border border-white/10">
              {isPlaying ? '● LIVE STREAM' : '❚❚ PAUSED'}
            </span>
          </div>
        </div>

        {/* Progress & Time */}
        <div className="flex flex-col gap-1.5 text-xs">
          <div className="flex justify-between font-bold text-[#3D3130]">
            <span>Session Progress ({progressPercent}%)</span>
            <span className="font-mono text-[#721C28] text-sm">
              {String(Math.floor(secondsRemaining / 60)).padStart(2, '0')}:{String(secondsRemaining % 60).padStart(2, '0')} remaining
            </span>
          </div>
          <div className="w-full bg-[#EFE9DF] h-2 rounded-full overflow-hidden border border-[#E5DDD0]">
            <div
              className="bg-gradient-to-r from-[#721C28] to-[#9E2A3C] h-full rounded-full transition-all duration-300"
              style={{ width: `${progressPercent}%` }}
            />
          </div>
        </div>

        {/* Routine Steps Checklist */}
        <div className="bg-white border border-[#E5DDD0] rounded-2xl p-3 flex flex-col gap-1.5 text-[11px] shadow-sm">
          <span className="font-extrabold text-[#7A6E6B] uppercase tracking-wider text-[10px]">Session Movements:</span>
          {session.steps.map((st, i) => (
            <div
              key={i}
              className={`flex items-center gap-2 ${
                i === currentStepIndex
                  ? 'text-[#721C28] font-bold'
                  : i < currentStepIndex
                  ? 'text-emerald-700 line-through opacity-70'
                  : 'text-[#7A6E6B]'
              }`}
            >
              <span>{i < currentStepIndex ? '✓' : i === currentStepIndex ? '▶' : '○'}</span>
              <span>{st}</span>
            </div>
          ))}
        </div>

        {/* Controls */}
        <div className="flex gap-3 pt-2">
          <button
            onClick={() => setIsPlaying(!isPlaying)}
            className="flex-1 py-3 bg-white hover:bg-[#F3EFE6] text-[#231815] font-extrabold rounded-full text-xs cursor-pointer border border-[#E5DDD0] transition-all flex items-center justify-center gap-2 shadow-sm"
          >
            {isPlaying ? <Pause className="w-4 h-4" /> : <Play className="w-4 h-4" />}
            <span>{isPlaying ? 'PAUSE STREAM' : 'RESUME STREAM'}</span>
          </button>

          <button
            onClick={onComplete}
            className="flex-1 py-3 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white font-black rounded-full text-xs cursor-pointer shadow-lg shadow-[#721C28]/20 transition-all uppercase tracking-wider flex items-center justify-center gap-2"
          >
            <CheckCircle2 className="w-4 h-4" />
            <span>MARK COMPLETE</span>
          </button>
        </div>

      </div>
    </div>
  )
}
