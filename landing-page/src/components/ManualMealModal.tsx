import { useState } from 'react'
import { Apple, X } from 'lucide-react'
import type { MealItem } from '../types/fitness'

interface ManualMealModalProps {
  isOpen: boolean
  onClose: () => void
  onSaveMeal: (meal: Omit<MealItem, 'id'>) => void
}

export const ManualMealModal: React.FC<ManualMealModalProps> = ({
  isOpen,
  onClose,
  onSaveMeal
}) => {
  const [name, setName] = useState('')
  const [portion, setPortion] = useState('1 Serving')
  const [calories, setCalories] = useState('350')
  const [protein, setProtein] = useState('25')
  const [carbs, setCarbs] = useState('35')
  const [fats, setFats] = useState('10')
  const [category, setCategory] = useState<'Breakfast' | 'Lunch' | 'Snacks' | 'Dinner'>('Lunch')

  if (!isOpen) return null

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!name.trim()) return

    onSaveMeal({
      name: name.trim(),
      portion: portion.trim() || '1 Serving',
      calories: parseInt(calories) || 300,
      protein: parseInt(protein) || 20,
      carbs: parseInt(carbs) || 30,
      fats: parseInt(fats) || 8,
      category
    })

    setName('')
    onClose()
  }

  return (
    <div className="fixed inset-0 z-50 bg-black/60 backdrop-blur-md flex items-center justify-center p-4">
      <div className="bg-[#FAF7F2] border border-[#E5DDD0] rounded-3xl max-w-md w-full p-6 sm:p-7 shadow-2xl flex flex-col gap-5 text-left">
        
        <div className="flex justify-between items-center border-b border-[#E8E1D5] pb-3">
          <div className="flex items-center gap-2.5">
            <div className="w-8 h-8 rounded-full bg-[#721C28] text-white flex items-center justify-center font-bold shadow-md shadow-[#721C28]/20">
              <Apple className="w-4 h-4" />
            </div>
            <div>
              <h3 className="font-serif font-black text-lg text-[#231815]">Log Custom Meal</h3>
              <p className="text-[11px] text-[#7A6E6B]">Add nutritional macros to your daily diet record</p>
            </div>
          </div>
          <button onClick={onClose} className="text-[#7A6E6B] hover:text-[#231815] p-1 rounded-full hover:bg-white">
            <X className="w-4 h-4" />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="flex flex-col gap-4 text-xs">
          
          <div className="flex flex-col gap-1.5">
            <label className="font-bold text-[#3D3130]">Food Item Name</label>
            <input
              type="text"
              placeholder="e.g. Scrambled Eggs with Avocado Toast"
              value={name}
              onChange={e => setName(e.target.value)}
              className="bg-white border border-[#E5DDD0] px-4 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              required
            />
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Meal Category</label>
              <select
                value={category}
                onChange={e => setCategory(e.target.value as any)}
                className="bg-white border border-[#E5DDD0] px-3 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              >
                <option value="Breakfast">Breakfast</option>
                <option value="Lunch">Lunch</option>
                <option value="Snacks">Snacks</option>
                <option value="Dinner">Dinner</option>
              </select>
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Portion / Serving</label>
              <input
                type="text"
                placeholder="e.g. 1 Plate / 200g"
                value={portion}
                onChange={e => setPortion(e.target.value)}
                className="bg-white border border-[#E5DDD0] px-3 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#3D3130]">Calories (kcal)</label>
              <input
                type="number"
                placeholder="350"
                value={calories}
                onChange={e => setCalories(e.target.value)}
                className="bg-white border border-[#E5DDD0] px-3 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
                required
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#721C28]">Protein (g)</label>
              <input
                type="number"
                placeholder="25"
                value={protein}
                onChange={e => setProtein(e.target.value)}
                className="bg-white border border-[#E5DDD0] px-3 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
                required
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-3">
            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#0D9488]">Carbohydrates (g)</label>
              <input
                type="number"
                placeholder="35"
                value={carbs}
                onChange={e => setCarbs(e.target.value)}
                className="bg-white border border-[#E5DDD0] px-3 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              />
            </div>

            <div className="flex flex-col gap-1.5">
              <label className="font-bold text-[#9D174D]">Fats (g)</label>
              <input
                type="number"
                placeholder="10"
                value={fats}
                onChange={e => setFats(e.target.value)}
                className="bg-white border border-[#E5DDD0] px-3 py-2.5 rounded-xl text-[#231815] focus:outline-none focus:border-[#721C28]"
              />
            </div>
          </div>

          <div className="flex justify-end gap-2 pt-3 border-t border-[#E8E1D5] mt-1">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 bg-white border border-[#E5DDD0] hover:bg-[#F3EFE6] text-[#3D3130] rounded-full font-bold cursor-pointer"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-5 py-2 bg-gradient-to-r from-[#721C28] to-[#8C2434] hover:from-[#50111A] hover:to-[#721C28] text-white font-black rounded-full cursor-pointer shadow-md shadow-[#721C28]/20 uppercase tracking-wider"
            >
              Save Meal Log
            </button>
          </div>

        </form>

      </div>
    </div>
  )
}
