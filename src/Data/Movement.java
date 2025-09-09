package Data;

public class Movement {
    private int id;
    private String name;
    private String displayName;
    private String description;
    private int power, accuracy, pp, maxPp, priority, chanceEffect;
    private Category category;
    private Type type;
    private Target target;

    private String flags;
    /*
      a. Is a contact move
      b. Affected by Protect
      c. Affected by Magic Coat
      d. Affected by Snatch
      e. Affected by Mirror Move
      f. Affected by King's Rock
      g. Sound-based move
      h. Pulse move
      i. Bomb move
      j. Biting move
      k. Dance move
      l. Explosive move
      m. Powder move
      n. Punching move
      o. Slicing move
      p. Wind move
      q. High critical ratio
      r. Always critical hit
     */

    private String effect;
    /*
      000. Nothing
      001. Effect varies with held item, Fling
      002. Traps target
      003. Secret Power, depending on environment
      004. Reduce last target move PP by 3
      005. User's Speed +1
      006. User's Special Attack +1
      007. User's Defense +2
      008. User's Defense +1
      009. User's Attack +1
      00A. Order Up
      00B. User's all stats +1
      00C. Prevents sound moves
      00D. Prevents recovering HP
      00E. Target's Speed -1
      00F. Target's Special Defense -2
      010. Target's Special Defense -1
      011. Target's Special Attack -1
      012. Target's Defense -1
      013. Target's Defense -1 or flinch
      014. Target's Attack -1
      015. Target's accuracy -1
      016. Sleep
      017. Poison, Paralysis or Sleep
      018. Poison
      019. Paralysis
      01A. Freeze
      01B. Flinch or paralysis
      01C. Flinch or freeze
      01D. Flinch or burn
      01E. Flinch
      01F. Confusion
      020. Burn, Freeze or Paralysis
      021. Burn
      022. Burn on increased stats
      023. Badly poisoned
      024. Salt Cure
      025. Cure burn
      026. Create Stealth Rock
      027. Create Spikes
      028. Create Psychic Terrain
      029. Syrup Bomb
      02A. Fixed 2 strikes
      02B. Fixed 3 strikes
      02C. 2-5 strikes
      02D. Accuracy dependent strikes
      02E. Beat Up
      02F. Water Shuriken
      030. Double Iron Bash (3 strikes and flinch)
      031. 2 strikes and can poison
      032. Population Bomb
      033. Pay Day
      034. Happy Hour
      035. Make it Rain
      036. Bind move
      037. Rampage
      038. Bide
      039. Uproar
      03A. Rollout
      03B. Metal Burst/Comeuppance
      03C. Counter
      03D. Mirror Coat
      03E. Electrify
      03F. Convert Normal to Electric moves
      040. Create Poison Spikes
      041. Sticky web
      042. Defog
      043. Mortal/Rapid spin
      044. Tidy Up
      045. Judgement
      046. HP Draining 50%
      047. HP Draining 75%
      048. Strength Sap
      049. Leech seed
      04A. Dream eater
      04B. Matcha gotcha
      04C. Bestow
      04D. Eat target's berry
      04E. Remove target's item
      04F. Steal target's item
      050. Embargo
      051. Disable
      052. Torment
      053. Taunt
      054. Heal Block
      055. Destroys target's berry
      056. Trick Room
      057. Magic Room
      058. Wonder Room
      059. Natural gift
      05A. Recycle
      05B. Stuff cheeks
      05C. Switch items
      05D. Teatime
      05E. Center of attention
      05F. Destiny bond
      060. Grudge
      061. Return
      062. Frustration
      063. More damage more weight
      064. More damage weight percent
      065. Autotomize
      066. Sky Drop
      067. Target's Attack -2
      068. Target's Defense -2
      069. Target's evasion -2
      06A. OHKO move
      06B. Captivate
      06C. Target's Special Attack -2
      06D. Memento
      06E. Target's Attack and Special Attack -1
      06F. Octolock
      070. Target's Speed -2
      071. Toxic Thread
      072. Tera Blast
      073. User's Attack and Defense -1
      074. User's Defense and Special Defense -1
      075. User's Defense -1
      076. 2-5 strikes + raise Speed + lowers Defense
      077. User's Defense, Special Defense and Speed -1
      078. Shell Smash
      079. User's Special Attack -2
      07A. Curse
      07B. User's Speed -2
      07C. User's Speed -1
      07D. Acupressure
      07E. User's Attack and Defense +1
      07F. User's Attack and Special Attack +2
      080. Gear Up
      081. Rototiller
      082. User's Attack +2 and Defense -1
      083. Swagger
      084. Flatter
      085. Flower Shield
      086. Magnetic Flux
      087. Target's Special Defense +1
      088. User's Attack, Defense and accuracy +1
      089. User's Attack and accuracy +1
      08A. Gravity
      08B. Belly Drum
      08C. Clangorous Soul
      08D. User's Attack and Speed +1
      08E. User's all stats +2
      08F. Fell stinger
      090. Fillet away
      091. User's Attack and Special Attack +1
      092. Ally's Attack +1
      093. No Retreat
      094. Rage
      095. User's Attack +2 and Speed +1
      096. User's Attack +2
      097. User's Attack, Defense and Speed +1
      098. User's Defense and Special Defense +1
      099. User's Defense +3
      09A. Stockpile
      09B. Swallow
      09C. Spit Up
      09D. Fixed damage
      09E. Damage = target level
      09F. User's evasion +1
      100. User's evasion +2
      101. User's Special Defense +2
      102. User's Special Attack and Special Defense +1
      103. Charge
      104. User's Special Attack and Special Defense +1 and cure status problem
      105. User's Special Attack, Special Defense and Speed +1
      106. User's Speed +2
      107. Aura Wheel
      108. Gastroacid
      109. Core Enforcer
      10A. Assist
      10B. Copycat
      10C. Me First
      10D. Metronome
      10E. Mirror Move
      10F. Nature Power
      110. Sleep Talk
      111. Snatch
      112. Crash damage for fail
      113. Crash damage for fail + can confuse
      114. Snore
      115. Upper Hand
      116. Choose physical or special + ignores abilities
      117. Choose physical or special + poison
      118. Ignores abilities
      119. Alluring voice
      11A. Aromatherapy/Heal Bell
      11B. Jungle Healing/Lunar Blessing
      11C. Psycho Shift
      11D. Purifiy
      11E. Refresh
      11F. Rest
      120. Cure paralysis
      121. Cure sleep
      122. Dragon Cheer
      123. Focus Energy
      124. Explosion
      125. Lunar Dance/Healing Wish
      126. Final Gambit
      127. Beak blast
      128. Burning bulwark
      129. Protect
      12A. Charging turn
      12B. Semi invulnerable turn
      12C. Infernal parade
      12D. Freeze Dry
      12E. Volt Tackle
      12F. Fire Blitz
      130. Yawn
      131. Burn up
      132. Camouflage
      133. Conversion
      134. Conversion2
      135. Double Shock
      136. Forest Curse/TrickOrTreat
      137. Magic Powder/Soak
      138. Reflect type
      139. Roost
      13A. Transform
      13B. Create Grassy Terrain
      13C. Create Misty Terrain
      13D. Create Electric Terrain
      13E. Ice spinner
      13F. Splintered Stormshards
      140. Steel roller
      141. Expanding force
      142. Floral Healing
      143. Grassy Glide
      144. Psyblade
      145. Rising voltage
      146. Terrain Pulse
      147. Hidden Power
      148. Raging bull
      149. Revelation Dance
      14A. Techno Blast
      14B. Weather Ball
      14C. Chloro Blast
      14D. Mind Blown/Steel Beam
      14E. Shed Tail
      14F. Substitute
      150. Endeavor
      151. Guardian of Alola
      152. Psywave
      153. Damage 50% rest HP (super fang, nature's madness)
      154. Destroys Protect
      155. Destroys Protect + reduce defense
      156. Destroys Protect + charging turn
      157. Recoil 1/3 damage
      158. Recoil 1/4 damage
      159. Recoil 1/2 damage
      15A. Struggle
      15B. Thousand Arrows
      15C. Flying Press
      15D. Mat Block
      15E. Belch
      15F. Barb Barrage
      160. More user HP more damage
      161. Less target HP, less damage
      162. More speed more damage
      163. Less user HP more damage
      164. Gyro ball
      165. Magnitude
      166. Power Trip
      167. Present
      168. Punishment
      169. Trump Card
      16A. Light that burns the sky
      16B. Acrobatics
      16C. Revenge/Avalanche
      16D. Assurance
      16E. Payback
      16F. Bolt Beak/Fishious Rend
      170. Brine
      171. If supereffective, more damage
      172. More damage if Dinamax
      173. Echoed voice
      174. Facade
      175. Hex
      176. Fickle Beam
      177. Pledges
      178. Fury cutter
      179. Fire/Bolt Fusion
      17A. Lash Out
      17B. Pursuit
      17C. Rage fist
      17D. Retaliate
      17E. Round
      17F. Stomping tantrum
      180. Venoshock
      181. Odor sleuth/Foresight
      182. Miracle eye
      183. Gravity
      184. Smack down
      185. Recharging move
      186. Aqua ring
      187. Ingrain
      188. Life dew
      189. Paint split
      18A. Pollen puff
      18B. Revival Blessing
      18C. Wish
      18D. Recover 50% HP
      18E. Recover HP with weather
      18F. Shore up
      190. Expulse target
      191. User return to PokeBall
      192. Baton pass
      193. Chilly reception
      194. Parting shot
      195. Teleport
      196. Use target Defense
      197. Secret sword
      198. Body press
      199. Foul play
      19A. No effect
      19B. Crafty shield
      19C. Quick Guard
      19D. Wide Guard
      19E. Aurora veil
      19F. Reflect
      200. Light screen
      201. Break screens
      202. Put hail
      203. Put sun
      204. Put sandstorm
      205. Put rain
      206. Put snow
      207. Jaw lock
      208. Fairy lock
      209. Can't faint target
      20A. Helping hand
      20B. Worry seed
      20C. Entrainment
      20D. Simple beam
      20E. Skill swap
      20F. Power swap
      210. Guard swap
      211. Speed swap
      212. Heart swap
      213. Tailwind
      214. Infatuates target
      215. Endure
      216. Psych up
     */

    public Movement(int id, String name, String displayName, String description, int power, int accuracy, int pp, int maxPp, int priority, int chanceEffect, Category category, Type type, Target target, String effect, String flags) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.power = power;
        this.accuracy = accuracy;
        this.pp = pp;
        this.maxPp = maxPp;
        this.priority = priority;
        this.chanceEffect = chanceEffect;
        this.category = category;
        this.type = type;
        this.target = target;
        this.effect = effect;
        this.flags = flags;
    }

    @Override
    public String toString() {
        /*return "Movement{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", power=" + power +
                ", accuracy=" + accuracy +
                ", pp=" + pp +
                ", maxPp=" + maxPp +
                ", priority=" + priority +
                ", chanceEffect=" + chanceEffect +
                ", category=" + category +
                ", type=" + type.getDisplayName() +
                ", target=" + target +
                ", effect='" + effect + '\'' +
                ", flags='" + flags + '\'' +
                '}';*/
        return name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
