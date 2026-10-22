import { useState } from 'react'
import {
  Scale,
  Camera,
  Sparkles,
  Upload,
  Calendar,
  RefreshCw,
  ArrowDownRight,
  ArrowUpRight
} from 'lucide-react'
import type { UserProfile, WeightRecord, ProgressPhotoItem } from '../types/fitness'

interface ProgressViewProps {
  userProfile: UserProfile
  weightLogs: WeightRecord[]
  onAddWeightLog: (weight: number) => void
  photosList: ProgressPhotoItem[]
  onAddPhoto: (position: 'Front' | 'Side' | 'Back', url: string) => void
}

export const ProgressView: React.FC<ProgressViewProps> = ({
  userProfile,
  weightLogs,
  onAddWeightLog,
  photosList,
  onAddPhoto
}) => {
  const [newWeight, setNewWeight] = useState('')
  const [activePhotoUploadPos, setActivePhotoUploadPos] = useState<'Front' | 'Side' | 'Back' | null>(null)
  const [isAnalyzingPhysique, setIsAnalyzingPhysique] = useState(false)
  const [physiqueReport, setPhysiqueReport] = useState<string | null>(null)

  const currentWeight = weightLogs.length > 0 ? weightLogs[weightLogs.length - 1].weight : userProfile.weight
  const initialWeight = weightLogs.length > 0 ? weightLogs[0].weight : userProfile.weight
  const weightDelta = Math.round((currentWeight - initialWeight) * 10) / 10
  const targetDistance = Math.round(Math.abs(currentWeight - userProfile.targetWeight) * 10) / 10

  const handleWeightSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    const val = parseFloat(newWeight)
    if (!isNaN(val) && val > 30 && val < 300) {
      onAddWeightLog(val)
      setNewWeight('')
    }
  }

  // Handle mock photo upload
  const handlePhotoUploadSim = (position: 'Front' | 'Side' | 'Back') => {
    setActivePhotoUploadPos(position)
    setTimeout(() => {
      const samplePhotos = {
        Front: 'https://images.unsplash.com/photo-1517838277536-f5f99be501cd?w=600&q=80',
        Side: 'https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?w=600&q=80',
        Back: 'https://images.unsplash.com/photo-1534438327276-14e5300c3a48?w=600&q=80'
      }
      onAddPhoto(position, samplePhotos[position])
      setActivePhotoUploadPos(null)
    }, 1500)
  }

  // Trigger AI Physique Analysis
  const handleAnalyzePhysique = () => {
    setIsAnalyzingPhysique(true)
    setTimeout(() => {
      setIsAnalyzingPhysique(false)
      setPhysiqueReport(
        `AI Physique Assessment for ${userProfile.name}:\n\n` +
        `• Body Fat Analysis: Visible reduction in abdominal visceral circumference detected compared to baseline logs.\n` +
        `• Muscle Definition: Noticeable shoulder cap development and upper lat taper width.\n` +
        `• Progress Score: 88/100 (High consistency compliance).\n` +
        `• Next 14-Day Prescription: Maintain current caloric pace, progressively add 2.5kg to compound movements, and keep hydration above ${userProfile.weight > 80 ? '4' : '3.5'} Liters daily.`
      )
    }, 2000)
  }

  const positions: Array<'Front' | 'Side' | 'Back'> = ['Front', 'Side', 'Back']

  return (
    <div className="flex flex-col gap-6 w-full max-w-7xl mx-auto pb-12 animate-in fade-in duration-300">
      
      {/* Header Row */}
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center gap-4 classic-card p-6 rounded-3xl shadow-sm">
        <div>
          <div className="flex items-center gap-2">
            <span className="text-[10px] font-extrabold text-[#721C28] bg-[#721C28]/10 border border-[#721C28]/25 px-3 py-0.5 rounded-full uppercase tracking-wider">
              Goal: {userProfile.fitnessGoal}
            </span>
            <span className="text-xs text-[#7A6E6B]">● Target: {userProfile.targetWeight} kg</span>
          </div>
          <h1 className="font-serif font-black text-2xl sm:text-3xl text-[#231815] tracking-tight mt-1">
            Progress & Transformation Hub
          </h1>
          <p className="text-[#5C4F4D] text-xs sm:text-sm mt-0.5">
            Log weigh-ins, check visual transformation milestones, and run AI physique audits
          </p>
        </div>

        {/* Action: Trigger AI Analysis */}
        <button
          onClick={handleAnalyzePhysique}
          disabled={isAnalyzingPhysique}
          className="flex items-center gap-2 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white font-extrabold px-6 py-3 rounded-full text-xs cursor-pointer shadow-lg shadow-[#721C28]/20 transition-all uppercase tracking-wider disabled:opacity-50"
        >
          {isAnalyzingPhysique ? (
            <>
              <RefreshCw className="w-4 h-4 animate-spin text-white" />
              <span>RUNNING AI SCAN...</span>
            </>
          ) : (
            <>
              <Sparkles className="w-4 h-4 text-white" />
              <span>AUDIT PHYSIQUE WITH AI</span>
            </>
          )}
        </button>
      </div>

      {/* AI Assessment Report Box if available */}
      {physiqueReport && (
        <div className="p-6 bg-[#FAF7F2] border border-[#721C28]/30 rounded-3xl text-[#231815] text-xs leading-relaxed whitespace-pre-line shadow-sm animate-in slide-in-from-top">
          <div className="flex items-center gap-2 mb-3">
            <div className="w-6 h-6 rounded-full bg-[#721C28] text-white flex items-center justify-center font-serif font-black text-xs">
              ✦
            </div>
            <h3 className="font-serif font-black text-sm text-[#721C28] uppercase tracking-wider">
              EchoFit AI Comprehensive Physique Audit
            </h3>
          </div>
          <p className="font-medium text-[#4A3D40] leading-relaxed">{physiqueReport}</p>
        </div>
      )}

      {/* Stat Cards: Weight Delta, Distance to Goal */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div className="p-5 classic-card rounded-3xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">Current Logged Weight</span>
          <span className="font-serif font-black text-3xl text-[#231815]">{currentWeight} kg</span>
          <span className="text-xs text-[#8A7D79] mt-1">Starting baseline: {initialWeight} kg</span>
        </div>

        <div className="p-5 classic-card rounded-3xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">Total Weight Delta</span>
          <div className="flex items-center gap-2">
            <span className="font-serif font-black text-3xl text-[#721C28]">
              {weightDelta > 0 ? `+${weightDelta}` : weightDelta} kg
            </span>
            {weightDelta < 0 ? (
              <ArrowDownRight className="w-6 h-6 text-emerald-600" />
            ) : (
              <ArrowUpRight className="w-6 h-6 text-[#721C28]" />
            )}
          </div>
          <span className="text-xs text-emerald-700 font-bold mt-1">
            {userProfile.fitnessGoal === 'Fat Loss' ? 'Favorable fat burn trend' : 'Consistent mass accrual'}
          </span>
        </div>

        <div className="p-5 classic-card rounded-3xl flex flex-col gap-1 shadow-sm">
          <span className="text-[10px] font-bold text-[#7A6E6B] uppercase">Distance to Target Weight</span>
          <span className="font-serif font-black text-3xl text-[#231815]">{targetDistance} kg</span>
          <span className="text-xs text-[#8A7D79] mt-1">Goal weight: {userProfile.targetWeight} kg</span>
        </div>
      </div>

      {/* Main Grid: Weight History Form & Photo Transformation Gallery */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-6">
        
        {/* Left Column (lg:col-span-5): Weight Logger & Historical List */}
        <div className="lg:col-span-5 classic-card p-6 rounded-3xl flex flex-col gap-5 shadow-sm">
          <div className="flex justify-between items-center">
            <h2 className="font-serif font-black text-lg text-[#231815] tracking-tight flex items-center gap-2">
              <Scale className="w-5 h-5 text-[#721C28]" /> Weight Tracking History
            </h2>
            <span className="text-xs font-bold text-[#7A6E6B]">{weightLogs.length} Records</span>
          </div>

          {/* Quick Add Form */}
          <form onSubmit={handleWeightSubmit} className="flex gap-2">
            <input
              type="number"
              step="0.1"
              placeholder="e.g. 74.2 kg"
              value={newWeight}
              onChange={e => setNewWeight(e.target.value)}
              className="flex-1 bg-[#FAF7F2] border border-[#E5DDD0] px-4 py-2.5 rounded-full text-xs text-[#231815] placeholder-[#8A7D79] focus:outline-none focus:border-[#721C28]"
              required
            />
            <button
              type="submit"
              className="bg-[#721C28] hover:bg-[#50111A] text-white px-5 py-2.5 rounded-full text-xs font-black cursor-pointer transition-all uppercase tracking-wider shadow-md"
            >
              Log Weight
            </button>
          </form>

          {/* Visual Weight History Table */}
          <div className="flex flex-col gap-2 max-h-[340px] overflow-y-auto pr-1">
            {weightLogs.slice().reverse().map((rec, i) => (
              <div
                key={i}
                className="p-3 bg-[#FAF7F2] border border-[#E5DDD0] rounded-2xl flex items-center justify-between text-xs hover:border-[#721C28]/40 transition-all shadow-sm"
              >
                <div className="flex items-center gap-2 text-[#5C4F4D]">
                  <Calendar className="w-3.5 h-3.5 text-[#721C28]" />
                  <span>{rec.date}</span>
                </div>
                <span className="font-serif font-black text-[#231815] text-sm">{rec.weight} kg</span>
              </div>
            ))}
          </div>
        </div>

        {/* Right Column (lg:col-span-7): 3-Pose Photo Check-in Gallery */}
        <div className="lg:col-span-7 classic-card p-6 rounded-3xl flex flex-col gap-5 shadow-sm">
          <div>
            <h2 className="font-serif font-black text-lg text-[#231815] tracking-tight flex items-center gap-2">
              <Camera className="w-5 h-5 text-[#721C28]" /> Physique Transformation Check-in
            </h2>
            <p className="text-xs text-[#7A6E6B] font-semibold mt-0.5">
              Consistent front, side, and back snapshots power AI body composition analysis
            </p>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            {positions.map(pos => {
              const photo = photosList.find(p => p.position === pos)
              const isUploading = activePhotoUploadPos === pos

              return (
                <div
                  key={pos}
                  className="bg-[#FAF7F2] border border-[#E5DDD0] rounded-2xl p-3 flex flex-col items-center gap-3 relative overflow-hidden group shadow-sm"
                >
                  <div className="w-full flex justify-between items-center text-[10px] font-black uppercase text-[#721C28]">
                    <span>{pos} Pose</span>
                    {photo && <span className="text-emerald-700">✓ Logged</span>}
                  </div>

                  {photo ? (
                    <div className="w-full h-44 rounded-xl overflow-hidden relative border border-[#E5DDD0] shadow-inner">
                      <img src={photo.url} alt={`${pos} physique`} className="w-full h-full object-cover" />
                      <div className="absolute inset-0 bg-black/60 opacity-0 group-hover:opacity-100 transition-opacity flex items-center justify-center p-2">
                        <button
                          onClick={() => handlePhotoUploadSim(pos)}
                          className="bg-[#721C28] text-white px-3.5 py-1.5 rounded-full text-xs font-black cursor-pointer shadow-lg"
                        >
                          Retake
                        </button>
                      </div>
                    </div>
                  ) : (
                    <div className="w-full h-44 rounded-xl bg-white border border-dashed border-[#DCD4C7] flex flex-col items-center justify-center p-4 text-center gap-2">
                      <Camera className="w-6 h-6 text-[#A59995]" />
                      <span className="text-[10px] text-[#7A6E6B] font-bold">No photo logged</span>
                    </div>
                  )}

                  <button
                    onClick={() => handlePhotoUploadSim(pos)}
                    disabled={isUploading}
                    className="w-full py-2 bg-white hover:bg-[#F3EFE6] text-[#231815] border border-[#E5DDD0] rounded-full text-[10px] font-black cursor-pointer transition-all uppercase tracking-wider flex items-center justify-center gap-1.5 disabled:opacity-50 shadow-sm"
                  >
                    {isUploading ? (
                      <>
                        <RefreshCw className="w-3 h-3 animate-spin text-[#721C28]" />
                        <span className="text-[#721C28]">Uploading...</span>
                      </>
                    ) : (
                      <>
                        <Upload className="w-3 h-3 text-[#721C28]" />
                        <span>{photo ? 'Update Photo' : 'Upload Pose'}</span>
                      </>
                    )}
                  </button>
                </div>
              )
            })}
          </div>
        </div>

      </div>

    </div>
  )
}
