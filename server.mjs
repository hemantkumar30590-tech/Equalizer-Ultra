import http from 'node:http';
import fs from 'node:fs';

const PORT = 3000;
const APK_PATHS = [
  '/app/applet/app/build/outputs/apk/debug/app-debug.apk',
  '/app/applet/.build-outputs/app-debug.apk',
  '/app/applet/Equltra-v1.0.apk',
];

function getApkPath() {
  for (const p of APK_PATHS) {
    if (fs.existsSync(p)) return p;
  }
  return null;
}

const HTML_PAGE = `<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Equltra — 31-Band ISO Studio EQ &amp; 2027 Quantum Audio Lab</title>
  <meta name="description" content="Equltra - 31-Band ISO 1/3-Octave System-wide EQ with YouTube Hook, 7-in-1 Future-Gen Quantum Lab, Sonar Scanner, Neural Brainwave 40Hz, 360 Soundstage Radar, Bio-Ear DNA &amp; Esports X-Ray." />
  <meta property="og:title" content="Equltra — 31-Band ISO Studio EQ &amp; 2027 Quantum Audio Lab" />
  <meta property="og:description" content="Equltra - 31-Band ISO 1/3-Octave System-wide EQ with YouTube Hook, 7-in-1 Future-Gen Quantum Lab, Sonar Scanner, Neural Brainwave 40Hz, 360 Soundstage Radar, Bio-Ear DNA &amp; Esports X-Ray." />
  <style>
    :root {
      --bg: #050608;
      --surface: #0D0F14;
      --card: #131720;
      --border: #202736;
      --cyan: #00F0FF;
      --magenta: #FF2A85;
      --violet: #8B5CF6;
      --emerald: #00E676;
      --amber: #FFB300;
      --text: #F4F7FF;
      --muted: #8A95AD;
    }
    * { box-sizing: border-box; margin: 0; padding: 0; }
    body {
      background: var(--bg);
      color: var(--text);
      font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Inter, monospace;
      min-height: 100vh;
      display: flex;
      flex-direction: column;
    }
    header {
      background: linear-gradient(180deg, #10141d 0%, #090b10 100%);
      border-bottom: 1px solid var(--border);
      padding: 14px 20px;
      display: flex;
      flex-wrap: wrap;
      align-items: center;
      justify-content: space-between;
      gap: 12px;
    }
    .brand { display: flex; align-items: center; gap: 12px; }
    .logo-badge {
      width: 40px; height: 40px; border-radius: 10px;
      background: linear-gradient(135deg, var(--cyan), var(--magenta));
      display: flex; align-items: center; justify-content: center;
      font-weight: 900; color: #050608; font-size: 16px;
    }
    .brand h1 { font-size: 18px; font-weight: 900; letter-spacing: 0.6px; }
    .brand p { font-size: 11px; color: var(--cyan); }
    .header-actions { display: flex; align-items: center; gap: 8px; flex-wrap: wrap; }
    .btn {
      border: 1px solid var(--border);
      background: var(--card);
      color: var(--text);
      padding: 8px 13px;
      border-radius: 8px;
      font-size: 12px;
      font-weight: 700;
      cursor: pointer;
      text-decoration: none;
      display: inline-flex;
      align-items: center;
      gap: 6px;
      transition: all 0.15s ease;
    }
    .btn:hover { border-color: var(--cyan); }
    .btn-primary {
      background: linear-gradient(90deg, var(--cyan), #00e676);
      color: #040608;
      border: none;
      font-weight: 900;
    }
    .btn-magenta {
      background: linear-gradient(90deg, var(--magenta), var(--violet));
      color: #fff;
      border: none;
    }
    main {
      max-width: 1240px;
      width: 100%;
      margin: 0 auto;
      padding: 16px;
      display: flex;
      flex-direction: column;
      gap: 14px;
      flex: 1;
    }
    .card {
      background: var(--card);
      border: 1px solid var(--border);
      border-radius: 12px;
      padding: 14px;
    }
    .quantum-card {
      background: linear-gradient(135deg, rgba(19,23,32,0.98), rgba(22,16,38,0.96));
      border: 1.5px solid var(--cyan);
      border-radius: 14px;
      padding: 15px;
    }
    .card-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 10px;
      flex-wrap: wrap;
      gap: 8px;
    }
    .card-title {
      font-size: 13px;
      font-weight: 800;
      text-transform: uppercase;
      letter-spacing: 0.8px;
      color: var(--cyan);
    }
    canvas#spectrumCanvas {
      width: 100%;
      height: 165px;
      background: #07090e;
      border-radius: 8px;
      border: 1px solid #1b2230;
      display: block;
    }
    .presets-row {
      display: flex;
      gap: 8px;
      overflow-x: auto;
      padding-bottom: 6px;
    }
    .chip {
      padding: 6px 12px;
      border-radius: 6px;
      background: var(--surface);
      border: 1px solid var(--border);
      color: var(--muted);
      font-size: 11.5px;
      font-weight: 700;
      cursor: pointer;
      white-space: nowrap;
    }
    .chip.active {
      background: rgba(0, 240, 255, 0.16);
      border-color: var(--cyan);
      color: var(--cyan);
    }
    .chip-vibe {
      border-color: rgba(139, 92, 246, 0.6);
      color: #e9d5ff;
    }
    .eq-scroll {
      display: flex;
      gap: 10px;
      overflow-x: auto;
      padding: 10px 4px;
    }
    .band-col {
      display: flex;
      flex-direction: column;
      align-items: center;
      min-width: 34px;
      gap: 6px;
    }
    .band-val { font-size: 10px; color: var(--cyan); font-family: monospace; }
    .band-slider {
      writing-mode: vertical-lr;
      direction: rtl;
      height: 125px;
      width: 18px;
      accent-color: var(--cyan);
      cursor: pointer;
    }
    .band-freq { font-size: 10px; color: var(--muted); font-family: monospace; }
    .quantum-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
      gap: 10px;
      margin-top: 10px;
    }
    .q-box {
      background: var(--surface);
      border: 1px solid var(--border);
      border-radius: 10px;
      padding: 11px;
    }
    .q-box h4 {
      font-size: 12px;
      color: var(--text);
      margin-bottom: 4px;
      display: flex;
      justify-content: space-between;
    }
    .q-box p {
      font-size: 10.5px;
      color: var(--muted);
      margin-bottom: 8px;
    }
    .dsp-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
      gap: 12px;
    }
    .knob-box {
      background: var(--surface);
      border: 1px solid var(--border);
      border-radius: 10px;
      padding: 12px;
    }
    .knob-box label {
      display: flex;
      justify-content: space-between;
      font-size: 12px;
      font-weight: 700;
      margin-bottom: 8px;
    }
    .knob-box input[type=range] { width: 100%; accent-color: var(--magenta); }
  </style>
</head>
<body>
  <header>
    <div class="brand">
      <div class="logo-badge">EQ</div>
      <div>
        <h1>EQULTRA v2.0 QUANTUM PRO</h1>
        <p>31-Band ISO EQ • 7-in-1 Future-Gen Quantum Lab • YouTube &amp; Global Android DSP</p>
      </div>
    </div>
    <div class="header-actions">
      <button class="btn" id="testAudioBtn" onclick="toggleSynth()">▶ Play Test Synth</button>
      <button class="btn btn-magenta" onclick="runSonarSweep()">📡 Sonar Room Scan</button>
      <a class="btn btn-primary" href="/download/Equltra-v1.0-PRO.apk" download="Equltra-v1.0-PRO.apk">⬇ Download Upgraded APK (Equltra-v1.0-PRO.apk)</a>
    </div>
  </header>

  <main>
    <!-- 🛸 2030 Sovereign R&D Lab (Japan • China • USA Next-Gen Roadmap) -->
    <div class="quantum-card" style="border-color: var(--emerald);">
      <div class="card-header">
        <div>
          <span class="card-title" style="color:var(--emerald);">🛸 2030 SOVEREIGN R&amp;D LAB (🇯🇵 JAPAN • 🇨🇳 CHINA • 🇺🇸 USA FUTURE ROADMAP — NEW!)</span>
          <div id="rnd2030Status" style="font-size:11.5px;color:var(--cyan);margin-top:3px;font-family:monospace;">
            🛸 [2030 SINGULARITY] • 🇯🇵 16Hz Phantom 88% • Doppler-0 ON • 🇨🇳 DSD1024 90% • Bio De-Esser ON • 🇺🇸 64-WFS 92% • De-Squash +5.2dB
          </div>
        </div>
        <div style="display:flex;gap:6px;flex-wrap:wrap;">
          <button class="chip" onclick="setRnd2030Mode('off', '2030 R&D Bypass', 0, 0, 0, 0)">⏹️ Bypass</button>
          <button class="chip" onclick="setRnd2030Mode('jp', '🇯🇵 Japan 2030: NTT 16Hz + Doppler-0', 92, 40, 40, 45)">🇯🇵 NTT 16Hz + Doppler-0</button>
          <button class="chip" onclick="setRnd2030Mode('cn', '🇨🇳 China 2030: DSD1024 + Back-EMF + De-Esser', 40, 94, 40, 45)">🇨🇳 DSD1024 + Back-EMF</button>
          <button class="chip" onclick="setRnd2030Mode('us', '🇺🇸 USA 2030: MIT 64-WFS + De-Squasher', 40, 45, 95, 92)">🇺🇸 MIT 64-WFS + De-Squash</button>
          <button class="chip active" onclick="setRnd2030Mode('singularity', '🛸 2030 Singularity (All 6 Future Labs)', 88, 90, 92, 86)">🛸 2030 Singularity (All 6)</button>
        </div>
      </div>

      <div class="quantum-grid">
        <div class="q-box">
          <h4><span>🇯🇵 NTT Phantom Sub-16Hz Neuro-Bass</span><span id="rnd16Val" style="color:var(--cyan);">88%</span></h4>
          <p>Missing-Fundamental 36/54/72Hz phase-locked overtones create 16Hz cinema sub-bass without driver overload</p>
          <input id="rnd16Slider" type="range" min="0" max="100" value="88" style="width:100%;accent-color:var(--cyan);" oninput="updateRnd2030Slider('p16', this.value)" />
          <div style="margin-top:6px;">
            <button class="chip active" id="dopplerBtn" onclick="toggleRndDoppler()">🫧 Yamaha Inverse Doppler Vocal Eraser: ON</button>
          </div>
        </div>
        <div class="q-box">
          <h4><span>🇨🇳 1-Bit DSD1024 + Back-EMF Damping</span><span id="rndDsdVal" style="color:var(--magenta);">90%</span></h4>
          <p>Liquid Sigma-Delta analog silk + Laser Back-EMF cone brake + Biometric 7kHz Sibilance De-Esser</p>
          <input id="rndDsdSlider" type="range" min="0" max="100" value="90" style="width:100%;accent-color:var(--magenta);" oninput="updateRnd2030Slider('dsd', this.value)" />
          <div style="margin-top:6px;">
            <button class="chip active" id="deEssBtn" onclick="toggleRndDeEsser()">🧬 Biometric Ear-Canal 7kHz De-Esser: ON</button>
          </div>
        </div>
        <div class="q-box">
          <h4><span>🇺🇸 MIT 64-Speaker WFS + De-Squasher</span><span id="rndWfsVal" style="color:var(--emerald);">92% | +5.2dB</span></h4>
          <p>64-Speaker Huygens Holographic Wall + YouTube/Spotify Crest-Factor Drum Slam Restorer</p>
          <input id="rndWfsSlider" type="range" min="0" max="100" value="92" style="width:100%;accent-color:var(--emerald);" oninput="updateRnd2030Slider('wfs', this.value)" />
          <input id="rndSqSlider" type="range" min="0" max="100" value="86" style="width:100%;accent-color:var(--amber);margin-top:6px;" oninput="updateRnd2030Slider('sq', this.value)" />
        </div>
      </div>
    </div>

    <!-- Global Triad Flagship Hi-Fi Suite (Japan • China • USA) -->
    <div class="quantum-card" style="border-color: var(--cyan);">
      <div class="card-header">
        <div>
          <span class="card-title" style="color:var(--cyan);">🌐 GLOBAL TRIAD FLAGSHIP HI-FI SUITE (🇯🇵 JAPAN • 🇨🇳 CHINA • 🇺🇸 USA — NEW!)</span>
          <div id="triadStatus" style="font-size:11.5px;color:var(--emerald);margin-top:3px;font-family:monospace;">
            🌐 [🇯🇵🇨🇳🇺🇸 TRIAD OMEGA FUSION] • 🇯🇵 DSEE + K2HD 85% • 🇨🇳 R2R + Beryllium 85% • 🇺🇸 Dirac + Sphere 85% • ISO 226 Loudness ON
          </div>
        </div>
        <div style="display:flex;gap:6px;flex-wrap:wrap;">
          <button class="chip" onclick="setTriadMode('off', 'Global Triad Off', 0, 0, 0)">⏹️ Off</button>
          <button class="chip" onclick="setTriadMode('japan', '🇯🇵 Japan: Sony DSEE + JVC K2HD + Yamaha Cinema', 88, 35, 35)">🇯🇵 Japan Sony/JVC/Yamaha</button>
          <button class="chip" onclick="setTriadMode('china', '🇨🇳 China: R2R Ladder + Beryllium Slam + Planar Air', 35, 90, 35)">🇨🇳 China R2R + Beryllium</button>
          <button class="chip" onclick="setTriadMode('usa', '🇺🇸 USA: Sphere 165K + Dirac Phase + THX Bass', 35, 40, 90)">🇺🇸 USA Sphere + Dirac + THX</button>
          <button class="chip active" onclick="setTriadMode('omega', '🇯🇵🇨🇳🇺🇸 Triad Omega Fusion', 85, 85, 85)">🌐 Triad Omega Fusion</button>
        </div>
      </div>

      <div class="quantum-grid">
        <div class="q-box">
          <h4><span>🇯🇵 Japan: Sony DSEE Ultimate + JVC K2HD</span><span id="jpDseeVal" style="color:#FF5252;">85%</span></h4>
          <p>12kHz–20kHz overtone resynthesis + Yamaha Cinema DSP front presence</p>
          <input id="jpDseeSlider" type="range" min="0" max="100" value="85" style="width:100%;accent-color:#FF5252;" oninput="updateTriadSlider('jp', this.value)" />
        </div>
        <div class="q-box">
          <h4><span>🇨🇳 China: Holo R2R + Beryllium Driver</span><span id="cnR2rVal" style="color:var(--amber);">85%</span></h4>
          <p>20–63Hz Beryllium piston slam + R2R warm vocal body + Planar ribbon air</p>
          <input id="cnR2rSlider" type="range" min="0" max="100" value="85" style="width:100%;accent-color:var(--amber);" oninput="updateTriadSlider('cn', this.value)" />
        </div>
        <div class="q-box">
          <h4><span>🇺🇸 USA: Sphere 165K + Dirac + THX AAA</span><span id="usDiracVal" style="color:var(--cyan);">85%</span></h4>
          <p>Dirac Impulse Phase Alignment + Vegas Sphere Holography + ISO 226 Loudness</p>
          <input id="usDiracSlider" type="range" min="0" max="100" value="85" style="width:100%;accent-color:var(--cyan);" oninput="updateTriadSlider('us', this.value)" />
          <div style="margin-top:6px;">
            <button class="chip active" id="isoLoudnessBtn" onclick="toggleIsoLoudness()">📈 ISO 226 Dynamic Equal-Loudness: ON</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Out-Of-Head Room Externalizer (Smyth A16 / OOHL Style) -->
    <div class="quantum-card" style="border-color: var(--amber);">
      <div class="card-header">
        <div>
          <span class="card-title" style="color:var(--amber);">🔊 OUT-OF-HEAD ROOM EXTERNALIZER (SMYTH A16 / OOHL ENGINE — NEW!)</span>
          <div id="oohStatus" style="font-size:11.5px;color:var(--emerald);margin-top:3px;font-family:monospace;">
            OUT-OF-HEAD [3.0M SOFA]: 3.0m Throw • Wall Bounce 58% • ITD 340µs Crossfeed 62% • Pinna 8kHz Notch ON • Floor Sub-Wave ON
          </div>
        </div>
        <div style="display:flex;gap:6px;flex-wrap:wrap;">
          <button class="chip" onclick="setOohMode('off', 'In-Ear Direct (Off)', 0.5, 0)">🎧 In-Ear Direct</button>
          <button class="chip" onclick="setOohMode('desk', '1.2m Studio Desk Monitors', 1.2, 38)">🖥️ 1.2m Studio Desk</button>
          <button class="chip active" onclick="setOohMode('sofa', '3.0m Living Room 5.1 Sofa', 3.0, 58)">🛋️ 3.0m Living Room Sofa</button>
          <button class="chip" onclick="setOohMode('imax', '6.0m IMAX Screen Throw', 6.0, 72)">🎬 6.0m IMAX Screen</button>
          <button class="chip" onclick="setOohMode('nextroom', 'Next-Room / Open-Door Party', 4.8, 85)">🚪 Next-Room Party Speaker</button>
          <button class="chip" onclick="setOohMode('lawn', 'Open-Air Lawn / Terrace Stage', 5.0, 44)">🌌 Open-Air Lawn Stage</button>
        </div>
      </div>

      <div class="quantum-grid">
        <div class="q-box">
          <h4><span>Speaker Distance Throw (Bahar Ki Doori)</span><span id="oohDistVal" style="color:var(--cyan);">3.0 Meters</span></h4>
          <p>Pushes vocals &amp; instruments 0.5m to 6.0m outside your head with HF air absorption</p>
          <input id="oohDistSlider" type="range" min="0.5" max="6.0" step="0.1" value="3.0" style="width:100%;accent-color:var(--cyan);" oninput="updateOohDist(this.value)" />
        </div>
        <div class="q-box">
          <h4><span>Early Room Wall &amp; Desk Bounce (8–22ms)</span><span id="oohRefVal" style="color:var(--amber);">58% Room Air</span></h4>
          <p>3-Tap Floor (8ms), Desk (14ms) &amp; Side-Wall (22ms) acoustic reflections</p>
          <input id="oohRefSlider" type="range" min="0" max="100" value="58" style="width:100%;accent-color:var(--amber);" oninput="updateOohReflection(this.value)" />
        </div>
        <div class="q-box">
          <h4><span>Pinna Concha 8kHz Notch &amp; Floor Sub-Wave</span><span id="oohHrtfVal" style="color:var(--emerald);">BOTH ACTIVE</span></h4>
          <p>340µs Skull-Shadow ITD + Outer-Ear Pinna Depth Notch + 40–63Hz Floor Resonance</p>
          <div style="display:flex;gap:6px;flex-wrap:wrap;">
            <button class="chip active" id="pinnaBtn" onclick="toggleOohPinna()">👂 Pinna 8kHz &amp; Chest HRTF: ON</button>
            <button class="chip active" id="floorSubBtn" onclick="toggleOohFloorSub()">🔊 Floor Sub-Wave: ON</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 8-in-1 Future-Gen Quantum Lab + 3D Multi-Instrument & Dual-Woofer Stage Architect -->
    <div class="quantum-card" style="border-color: var(--magenta);">
      <div class="card-header">
        <div>
          <span class="card-title" style="color:var(--magenta);">🎛️ 3D MULTI-INSTRUMENT &amp; VIRTUAL 7.1.4 DUAL-WOOFER STAGE ARCHITECT</span>
          <div id="stageTelemetry" style="font-size:11.5px;color:var(--cyan);margin-top:3px;font-family:monospace;">
            Live Concert Arena • 2 Woofers Active (X-Over 90Hz • Phase 0°) • Sub L/R +0% | Mid +0% | Air +0%
          </div>
        </div>
        <div style="display:flex;gap:6px;flex-wrap:wrap;">
          <button class="chip active" onclick="setStageFormation('arena')">🏟️ Live Concert Arena</button>
          <button class="chip" onclick="setStageFormation('drummer')">🥁 Drummer's Throne</button>
          <button class="chip" onclick="setStageFormation('maybach')">🚘 Maybach 4D Cabin</button>
          <button class="chip" onclick="setStageFormation('club360')">🔊 360° Club Dual-Woofer</button>
          <button class="chip" onclick="setStageFormation('split')">🎧 Binaural Stem Split</button>
        </div>
      </div>

      <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(290px,1fr));gap:14px;align-items:center;">
        <div style="background:#06080d;border:1px solid var(--border);border-radius:12px;padding:10px;text-align:center;">
          <div style="font-size:11px;color:var(--muted);margin-bottom:6px;">
            DRAG ANY INSTRUMENT, SPEAKER OR SUBWOOFER AROUND YOUR HEAD (360° ROOM FLOOR)
          </div>
          <canvas id="stageCanvas" width="340" height="270" style="width:100%;max-width:340px;height:270px;background:#05070b;border-radius:10px;border:1px solid #1e2636;cursor:grab;touch-action:none;"></canvas>
        </div>

        <div style="display:flex;flex-direction:column;gap:10px;">
          <div class="q-box">
            <h4>
              <span id="selNodeTitle" style="color:var(--cyan);">Selected: Front 18&quot; Subwoofer (SUB-1)</span>
              <span id="selNodeCoords" style="color:var(--emerald);font-family:monospace;">Azimuth -24° • Dist 55%</span>
            </h4>
            <p id="selNodeBand">20Hz – 90Hz Deep Sub-Bass Woofer Cone • Drag node on radar or tune below</p>
            <label style="display:flex;justify-content:space-between;font-size:11px;margin-bottom:4px;">
              <span>Node Acoustic Gain</span>
              <span id="selNodeGainVal" style="color:var(--cyan);font-family:monospace;">+3.5 dB</span>
            </label>
            <input id="selNodeGainSlider" type="range" min="-12" max="12" step="0.5" value="3.5" style="width:100%;accent-color:var(--cyan);" oninput="updateSelectedNodeGain(this.value)" />
            <div style="display:flex;gap:6px;margin-top:8px;flex-wrap:wrap;">
              <button class="btn" onclick="toggleSelectedMute()" id="muteNodeBtn">🔇 Mute Node</button>
              <button class="btn" onclick="toggleSelectedSolo()" id="soloNodeBtn">🎯 Solo Node</button>
              <button class="btn" onclick="centerSelectedNode()">🎯 Snap Front Center</button>
            </div>
          </div>

          <div class="q-box">
            <h4>
              <span>Dual-Woofer Crossover &amp; Phase Engine</span>
              <span id="xoverVal" style="color:var(--magenta);font-family:monospace;">90 Hz • 0° Phase</span>
            </h4>
            <p>Controls Sub-1 (Front) + Sub-2 (Rear Trunk) low-pass cutoff &amp; polarity alignment</p>
            <input type="range" min="45" max="160" value="90" style="width:100%;accent-color:var(--magenta);" oninput="updateCrossover(this.value)" />
            <div style="display:flex;gap:8px;margin-top:8px;flex-wrap:wrap;">
              <button class="chip active" id="phaseBtn" onclick="toggleWooferPhase()">⚡ Woofer Phase: 0° Normal</button>
              <button class="chip active" id="vocalLockBtn" onclick="toggleVocalLock()">🎤 Center Vocal Lock: ON</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 2030 Hyper-Matrix 10-Engine DSP Suite -->
    <div class="quantum-card" style="border-color: var(--emerald);">
      <div class="card-header">
        <div>
          <span class="card-title" style="color:var(--emerald);">🚀 2030 HYPER-MATRIX 10-ENGINE DSP SUITE (NEW UPGRADE)</span>
          <div id="hyperMatrixStatus" style="font-size:11.5px;color:var(--cyan);margin-top:3px;font-family:monospace;">
            18" SubSynth: 45% • 4-Stem[V:100% D:100% B:100% M:100%] • 4D Gyro: OFF • Crossfeed: 30%
          </div>
        </div>
        <div style="display:flex;gap:6px;flex-wrap:wrap;">
          <button class="chip active" onclick="applyStemMode('balanced')">🎚️ 100% Balanced</button>
          <button class="chip" onclick="applyStemMode('karaoke')">🎤 Karaoke (Vocal Kill)</button>
          <button class="chip" onclick="applyStemMode('vocal2x')">🎙️ 200% Vocal Isolate</button>
          <button class="chip" onclick="applyStemMode('dnb')">🥁 Drum &amp; 808 Bass Solo</button>
          <button class="btn btn-primary" onclick="captureRefDna()">🧬 10. Clone Song Mix DNA</button>
        </div>
      </div>

      <div class="quantum-grid">
        <div class="q-box">
          <h4><span>1. 4D Gyro Head-Tracking</span><span id="gyroVal" style="color:var(--cyan);">STAGE LOCK</span></h4>
          <p>Locks Vocals North or Subwoofer to Chest while turning head</p>
          <div style="display:flex;gap:4px;flex-wrap:wrap;">
            <button class="chip" onclick="setGyro('OFF')">Off</button>
            <button class="chip active" onclick="setGyro('STAGE LOCK')">Stage Lock</button>
            <button class="chip" onclick="setGyro('CHEST SUB')">Chest Woofer</button>
            <button class="chip" onclick="setGyro('4D IMAX')">4D IMAX</button>
          </div>
        </div>

        <div class="q-box">
          <h4><span>2 &amp; 8. 18&quot; Sub-Harmonic + Haptic</span><span id="subSynthVal" style="color:var(--magenta);">45% • HAPTIC ON</span></h4>
          <p>24Hz–45Hz Missing-Fundamental Octave + Phone Bass Vibration</p>
          <input type="range" min="0" max="100" value="45" style="width:100%;accent-color:var(--magenta);" oninput="updateSubSynth(this.value)" />
        </div>

        <div class="q-box">
          <h4><span>4 &amp; 5. Speed Cruise + 12AX7 Tube</span><span id="cruiseTubeVal" style="color:var(--amber);">110 KM/H • TUBE</span></h4>
          <p>Road-noise auto-boost + Vacuum Tube / Abbey Tape warmth</p>
          <div style="display:flex;gap:4px;flex-wrap:wrap;">
            <button class="chip" onclick="setCruiseTube('45 KM/H City')">🚗 45 km/h</button>
            <button class="chip active" onclick="setCruiseTube('110 KM/H Highway')">🏎️ 110 km/h</button>
            <button class="chip" onclick="setCruiseTube('12AX7 Tube Warm')">🔥 12AX7 Tube</button>
            <button class="chip" onclick="setCruiseTube('Reel Tape Glue')">📼 Tape Glue</button>
          </div>
        </div>

        <div class="q-box">
          <h4><span>6 &amp; 7. Tinnitus Shield + Crossfeed</span><span id="shieldVal" style="color:var(--emerald);">7.5kHz • 30%</span></h4>
          <p>Suppresses harsh 6–9kHz sibilance + 280µs Studio Monitor Matrix</p>
          <div style="display:flex;gap:4px;flex-wrap:wrap;">
            <button class="chip active" onclick="toggleTinnitusWeb()">🛡️ Tinnitus Shield: ON</button>
            <button class="chip" onclick="setVenueWeb('Burmester 4D S-Class')">🚘 S-Class 4D</button>
            <button class="chip" onclick="setVenueWeb('Berghain Sub Bunker')">🔊 Berghain</button>
            <button class="chip" onclick="setVenueWeb('Dolby Cinema Row G')">🎬 Dolby Row G</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 8-in-1 Future-Gen Quantum Lab Card -->
    <div class="quantum-card">
      <div class="card-header">
        <div>
          <span class="card-title">⚡ 8-IN-1 FUTURE-GEN QUANTUM AUDIO LAB (2027 EDITION)</span>
          <div id="quantumStatus" style="font-size:11.5px;color:var(--emerald);margin-top:3px;font-family:monospace;">
            SONAR READY • Bio-Ear Age: 20 Yrs (92% Golden Score) • 360° Azimuth: 0°
          </div>
        </div>
        <div style="display:flex;gap:6px;flex-wrap:wrap;">
          <button class="btn" onclick="runSonarSweep()">1. Sonar Sweep</button>
          <button class="btn" id="riderBtn" onclick="toggleAutoRider()">5. AI Auto-Rider: OFF</button>
          <button class="btn" id="xrayBtn" onclick="toggleEsportsXray()">6. Esports X-Ray: OFF</button>
        </div>
      </div>

      <div class="presets-row" style="margin-bottom:8px;">
        <button class="chip chip-vibe" onclick="applyVibe('rolls')">✨ Rolls Royce Midnight Rain</button>
        <button class="chip chip-vibe" onclick="applyVibe('cyberpunk')">✨ Cyberpunk 2077 Club Front Row</button>
        <button class="chip chip-vibe" onclick="applyVibe('imax')">✨ IMAX Interstellar Organ Sub</button>
        <button class="chip chip-vibe" onclick="applyVibe('punjabi')">✨ Punjabi Arena Front Row</button>
        <button class="chip chip-vibe" onclick="applyVibe('tokyo')">✨ Tokyo Rooftop Lo-Fi 3AM</button>
      </div>

      <div class="quantum-grid">
        <div class="q-box">
          <h4><span>2. Neural Brainwave DSP</span><span id="bwLabel" style="color:var(--violet);">OFF</span></h4>
          <p>Phase-locked binaural entrainment injected into audio stream</p>
          <div style="display:flex;gap:4px;flex-wrap:wrap;">
            <button class="chip" onclick="setBrainwave('OFF', 0)">Off</button>
            <button class="chip" onclick="setBrainwave('GAMMA 40Hz', 40)">40Hz Focus</button>
            <button class="chip" onclick="setBrainwave('ALPHA 10Hz', 10)">10Hz Flow</button>
            <button class="chip" onclick="setBrainwave('THETA 6Hz', 6)">6Hz Meditate</button>
            <button class="chip" onclick="setBrainwave('DELTA 2Hz', 2.2)">2Hz Sleep</button>
          </div>
        </div>

        <div class="q-box">
          <h4><span>3. 360° Soundstage Radar</span><span id="azimuthVal" style="color:var(--cyan);">0° Center</span></h4>
          <p>ITD/ILD binaural speaker orbit &amp; L/R audiophile balance</p>
          <input type="range" min="-180" max="180" value="0" style="width:100%;accent-color:var(--cyan);" oninput="updateAzimuth(this.value)" />
        </div>

        <div class="q-box">
          <h4><span>4. Bio-Ear Golden Hearing ID</span><span id="earAgeVal" style="color:var(--amber);">20 Yrs (92%)</span></h4>
          <p>Tap pure frequency tone &amp; synthesize Personal DNA 31-Band EQ</p>
          <div style="display:flex;gap:4px;flex-wrap:wrap;">
            <button class="chip" onclick="playTone(1000)">🔊 1kHz</button>
            <button class="chip" onclick="playTone(8000)">🔊 8kHz</button>
            <button class="chip" onclick="playTone(14000)">🔊 14kHz</button>
            <button class="chip active" onclick="synthesizeEarDna()">🧬 Apply Ear DNA</button>
          </div>
        </div>
      </div>
    </div>

    <div class="card">
      <div class="card-header">
        <span class="card-title">Real-Time 31-Band RTA Spectrum &amp; Frequency Curve</span>
        <span id="telemetryText" style="font-size:11px;color:var(--muted);font-family:monospace;">Dominant: 125 Hz | Peak: -5.8 dBFS | Preamp: -2.0 dB</span>
      </div>
      <canvas id="spectrumCanvas" width="1100" height="165"></canvas>
    </div>

    <div class="card">
      <div class="card-header">
        <span class="card-title">Studio &amp; Device Presets</span>
        <button class="btn" onclick="selectPreset('Flat')">Reset Flat</button>
      </div>
      <div class="presets-row" id="presetBar"></div>
    </div>

    <div class="card">
      <div class="card-header">
        <span class="card-title">31-Band ISO 1/3-Octave Graphic Equalizer (-15 dB to +15 dB)</span>
        <span style="font-size:11px;color:var(--muted);">20Hz – 20kHz Precision Mastering Faders</span>
      </div>
      <div class="eq-scroll" id="bandsContainer"></div>
    </div>

    <div class="dsp-grid">
      <div class="knob-box">
        <label><span>Sub-Harmonic Bass Boost</span><span id="bassVal">78%</span></label>
        <input type="range" min="0" max="100" value="78" oninput="updateDsp('bass', this.value)" />
      </div>
      <div class="knob-box">
        <label><span>Binaural Spatial 3D</span><span id="spatialVal">35%</span></label>
        <input type="range" min="0" max="100" value="35" oninput="updateDsp('spatial', this.value)" />
      </div>
      <div class="knob-box">
        <label><span>Acoustic Reverb Mix</span><span id="reverbVal">12%</span></label>
        <input type="range" min="0" max="100" value="12" oninput="updateDsp('reverb', this.value)" />
      </div>
      <div class="knob-box">
        <label><span>Preamp Headroom</span><span id="preampVal">-2.0 dB</span></label>
        <input type="range" min="-12" max="12" step="0.5" value="-2" oninput="updateDsp('preamp', this.value)" />
      </div>
    </div>
  </main>

  <script>
    const FREQS = [
      "20","25","31.5","40","50","63","80","100","125","160",
      "200","250","315","400","500","630","800","1k","1.25k","1.6k",
      "2k","2.5k","3.15k","4k","5k","6.3k","8k","10k","12.5k","16k","20k"
    ];
    const PRESETS = {
      "Bass Boost": [7,7.5,8,7.5,6.5,5.5,4,2.5,1,0,0,-0.5,-1,-1,-0.5,0,0.5,1,1.5,2,2.5,2.5,2,2,2.5,3,3,2.5,2,1.5,1],
      "Flat": Array(31).fill(0),
      "AirPods Pro 3": [4.5,5,5.2,4.8,4,3,2,1,0.5,0,-0.5,-1,-1.2,-0.8,-0.4,0,0.6,1.2,1.8,2.2,2.5,2.2,1.8,1.5,2,2.8,3.2,3,2.5,2,1.5],
      "Sony WH-1000XM6": [3.5,4,4.2,3.5,2.5,1.5,0.5,-0.5,-1.5,-2,-2,-1.5,-0.5,0.5,1,1.5,2,2.5,3,3.2,3,2.5,2,2,2.5,3,3.5,3.2,2.5,2,1.5],
      "Theater 3D": [6,6.5,7,6.5,5.5,4.5,3,1.5,0,-1,-1.5,-1.5,-1,0,1,2,2.8,3.2,3.5,3.2,2.8,2.5,2.5,3,3.5,4,4,3.5,3,2.5,2],
      "Vocal Clarity": [-2,-2,-1.5,-1,-0.5,0,0.5,1,1.5,2,2.5,3,3.5,4,4.5,5,5,4.5,4,3.5,3,2.5,2,1.5,1,1,0.5,0,0,-0.5,-1]
    };

    let currentPreset = "Bass Boost";
    let gains = [...PRESETS["Bass Boost"]];
    let dsp = { bass: 78, spatial: 35, reverb: 12, preamp: -2.0, azimuth: 0, brainwaveHz: 0 };
    let playing = false;
    let autoRider = false;
    let esportsXray = false;
    let audioCtx = null;
    let synthInterval = null;
    let tick = 0;

    function ensureAudio() {
      if (!audioCtx) audioCtx = new (window.AudioContext || window.webkitAudioContext)();
      return audioCtx;
    }

    function runSonarSweep() {
      const ctx = ensureAudio();
      const osc = ctx.createOscillator();
      const gain = ctx.createGain();
      osc.type = "sine";
      osc.frequency.setValueAtTime(50, ctx.currentTime);
      osc.frequency.exponentialRampToValueAtTime(11000, ctx.currentTime + 0.85);
      gain.gain.setValueAtTime(0.14, ctx.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 0.88);
      osc.connect(gain);
      gain.connect(ctx.destination);
      osc.start();
      osc.stop(ctx.currentTime + 0.9);

      gains = gains.map((_, i) => {
        if (i <= 6) return +(5.0 - i * 0.4).toFixed(1);
        if (i >= 9 && i <= 13) return -1.8;
        if (i >= 18 && i <= 24) return 2.4;
        if (i >= 25) return 2.8;
        return 0.5;
      });
      currentPreset = "Sonar Room Calibrated";
      PRESETS[currentPreset] = [...gains];
      document.getElementById("quantumStatus").textContent =
        "📡 SONAR CALIBRATION COMPLETE • Seal 96% • Standing Wave @ 160Hz Suppressed • RT60 280ms";
      renderPresets();
      renderBands();
    }

    function setBrainwave(label, hz) {
      dsp.brainwaveHz = hz;
      document.getElementById("bwLabel").textContent = label;
      document.getElementById("quantumStatus").textContent =
        "🧠 NEURAL BRAINWAVE ENTRAINMENT: " + label + " ACTIVE";
      if (hz > 0) {
        const ctx = ensureAudio();
        const oscL = ctx.createOscillator();
        const oscR = ctx.createOscillator();
        const g = ctx.createGain();
        oscL.frequency.value = 200;
        oscR.frequency.value = 200 + hz;
        g.gain.setValueAtTime(0.05, ctx.currentTime);
        g.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 0.6);
        oscL.connect(g);
        oscR.connect(g);
        g.connect(ctx.destination);
        oscL.start();
        oscR.start();
        oscL.stop(ctx.currentTime + 0.62);
        oscR.stop(ctx.currentTime + 0.62);
      }
    }

    function updateAzimuth(deg) {
      dsp.azimuth = parseFloat(deg);
      document.getElementById("azimuthVal").textContent = deg + "°";
      document.getElementById("quantumStatus").textContent =
        "🌐 360° SOUNDSTAGE RADAR AZIMUTH: " + deg + "° (ITD/ILD Binaural Matrix)";
    }

    function playTone(hz) {
      const ctx = ensureAudio();
      const osc = ctx.createOscillator();
      const g = ctx.createGain();
      osc.frequency.value = hz;
      g.gain.setValueAtTime(0.1, ctx.currentTime);
      g.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 0.4);
      osc.connect(g);
      g.connect(ctx.destination);
      osc.start();
      osc.stop(ctx.currentTime + 0.42);
    }

    function synthesizeEarDna() {
      gains = [
        4.2,4.0,3.6,3.2,2.6,2.0,1.4,0.8,0.4,0,-0.4,-0.6,-0.4,0,0.6,
        1.2,1.8,2.2,2.6,3.0,3.2,3.4,3.6,3.8,4.2,4.5,4.8,5.0,5.2,5.4,5.0
      ];
      currentPreset = "Bio-Ear DNA (19Y Golden)";
      PRESETS[currentPreset] = [...gains];
      document.getElementById("earAgeVal").textContent = "19 Yrs (96%)";
      document.getElementById("quantumStatus").textContent =
        "🧬 BIO-EAR DNA APPLIED • Estimated Ear Age: 19 Yrs (Bat-Ear Golden Hearing)";
      renderPresets();
      renderBands();
    }

    function toggleAutoRider() {
      autoRider = !autoRider;
      document.getElementById("riderBtn").textContent = "5. AI Auto-Rider: " + (autoRider ? "ON" : "OFF");
      document.getElementById("quantumStatus").textContent = autoRider
        ? "⚡ DYNAMIC AI AUTO-RIDER ACTIVE • Real-time vocal & sub-bass spectral morphing"
        : "AI AUTO-RIDER STANDBY";
    }

    function toggleEsportsXray() {
      esportsXray = !esportsXray;
      document.getElementById("xrayBtn").textContent = "6. Esports X-Ray: " + (esportsXray ? "ON" : "OFF");
      if (esportsXray) {
        gains = [-3.5,-3,-2.5,-2,-1.5,-2,-2.8,-3.5,-4,-3.8,-3.2,-2.5,-1.5,0,1.5,3,4.5,5.8,6.8,7.5,8,8.2,7.8,6.8,5.5,4.2,3.5,2.8,2,1.2,0.5];
        currentPreset = "Esports Tactical X-Ray";
        PRESETS[currentPreset] = [...gains];
        document.getElementById("quantumStatus").textContent =
          "🎯 ESPORTS TACTICAL X-RAY ACTIVE • 1.6kHz–4kHz Footsteps & Reload Cues Boosted (+8.2 dB)";
        renderPresets();
        renderBands();
      }
    }

    function applyVibe(type) {
      const map = {
        rolls: { name: "Rolls Royce Midnight Rain", gains: [6.5,7,7.2,6.8,6,5,3.8,2.5,1.4,0.6,0.2,-0.4,-0.8,-1,-0.6,0,0.6,1.2,1.8,2.2,2.5,2.2,1.8,1.4,1.8,2.4,2.8,2.6,2.2,1.8,1.4] },
        cyberpunk: { name: "Cyberpunk 2077 Club Front Row", gains: [8.5,9.2,9.5,8.8,7.6,6.2,4.4,2.4,0.8,-0.6,-1.4,-1.8,-1.5,-0.8,0,0.8,1.6,2.4,3,3.4,3.6,3.2,2.8,3.2,4,4.6,4.8,4.4,3.8,3.2,2.6] },
        imax: { name: "IMAX Interstellar Organ Sub", gains: [9.5,9.5,8.8,7.8,6.5,5,3.5,2,0.8,0,-0.6,-1,-1.2,-0.8,-0.2,0.5,1.2,2,2.6,3,3.2,3,2.6,2.4,2.8,3.4,3.8,3.6,3.2,2.8,2.4] },
        punjabi: { name: "Punjabi Arena Front Row", gains: [7.2,8,8.6,8.8,8.2,7,5.4,3.6,1.8,0.4,-0.6,-1.2,-1,-0.2,0.8,1.8,2.8,3.6,4.2,4.4,4,3.4,2.8,2.6,3,3.4,3.6,3.2,2.6,2,1.5] },
        tokyo: { name: "Tokyo Rooftop Lo-Fi 3AM", gains: [4,4.6,5,5.2,4.8,4.2,3.6,3,2.4,2,1.6,1.2,0.8,0.5,0.4,0.6,1,1.4,1.6,1.4,1,0.5,0,-0.5,-1,-1.4,-1.8,-2,-2.2,-2.5,-3] }
      };
      const v = map[type];
      if (!v) return;
      currentPreset = v.name;
      gains = [...v.gains];
      PRESETS[currentPreset] = [...gains];
      document.getElementById("quantumStatus").textContent = "✨ HOLOGRAPHIC VIBE APPLIED: " + v.name;
      renderPresets();
      renderBands();
    }

    function renderPresets() {
      const bar = document.getElementById("presetBar");
      bar.innerHTML = "";
      Object.keys(PRESETS).forEach(name => {
        const btn = document.createElement("button");
        btn.className = "chip" + (name === currentPreset ? " active" : "");
        btn.textContent = name;
        btn.onclick = () => selectPreset(name);
        bar.appendChild(btn);
      });
    }

    function renderBands() {
      const container = document.getElementById("bandsContainer");
      container.innerHTML = "";
      FREQS.forEach((f, idx) => {
        const col = document.createElement("div");
        col.className = "band-col";
        const val = document.createElement("div");
        val.className = "band-val";
        val.id = "bval-" + idx;
        val.textContent = (gains[idx] >= 0 ? "+" : "") + gains[idx].toFixed(1);

        const slider = document.createElement("input");
        slider.type = "range";
        slider.className = "band-slider";
        slider.min = "-15";
        slider.max = "15";
        slider.step = "0.5";
        slider.value = gains[idx];
        slider.oninput = (e) => {
          gains[idx] = parseFloat(e.target.value);
          val.textContent = (gains[idx] >= 0 ? "+" : "") + gains[idx].toFixed(1);
        };

        const lbl = document.createElement("div");
        lbl.className = "band-freq";
        lbl.textContent = f;

        col.appendChild(val);
        col.appendChild(slider);
        col.appendChild(lbl);
        container.appendChild(col);
      });
    }

    function selectPreset(name) {
      currentPreset = name;
      gains = [...PRESETS[name]];
      renderPresets();
      renderBands();
    }

    function updateDsp(key, val) {
      dsp[key] = parseFloat(val);
      if (key === 'bass') document.getElementById('bassVal').textContent = val + '%';
      if (key === 'spatial') document.getElementById('spatialVal').textContent = val + '%';
      if (key === 'reverb') document.getElementById('reverbVal').textContent = val + '%';
      if (key === 'preamp') document.getElementById('preampVal').textContent = parseFloat(val).toFixed(1) + ' dB';
    }

    function toggleSynth() {
      const btn = document.getElementById("testAudioBtn");
      if (playing) {
        playing = false;
        btn.textContent = "▶ Play Test Synth";
        if (synthInterval) clearInterval(synthInterval);
        return;
      }
      playing = true;
      btn.textContent = "⏹ Stop Test Synth";
      const ctx = ensureAudio();
      let step = 0;
      const notes = [55, 55, 65.41, 73.42, 110, 146.83, 220, 329.63];
      synthInterval = setInterval(() => {
        if (!playing) return;
        const osc = ctx.createOscillator();
        const gain = ctx.createGain();
        const freq = notes[step % notes.length];
        osc.type = step % 2 === 0 ? "sawtooth" : "triangle";
        osc.frequency.setValueAtTime(freq, ctx.currentTime);
        const subBoost = 1 + (dsp.bass / 100) * 0.6;
        gain.gain.setValueAtTime(0.08 * subBoost, ctx.currentTime);
        gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 0.22);
        osc.connect(gain);
        gain.connect(ctx.destination);
        osc.start();
        osc.stop(ctx.currentTime + 0.23);
        step++;
      }, 240);
    }

    const canvas = document.getElementById("spectrumCanvas");
    const ctx = canvas.getContext("2d");
    function drawSpectrum() {
      tick += playing ? 0.16 : 0.07;
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      const w = canvas.width;
      const h = canvas.height;
      const barW = (w - 40) / 31;

      ctx.strokeStyle = "#182030";
      ctx.lineWidth = 1;
      for (let y = 25; y < h; y += 35) {
        ctx.beginPath();
        ctx.moveTo(0, y);
        ctx.lineTo(w, y);
        ctx.stroke();
      }

      ctx.beginPath();
      for (let i = 0; i < 31; i++) {
        const riderDelta = autoRider ? Math.sin(tick * 0.5 + i * 0.3) * 1.2 : 0;
        const gVal = Math.max(-15, Math.min(15, gains[i] + riderDelta));
        const norm = (gVal + 15) / 30;
        const pulse = (playing ? 0.22 : 0.08) * Math.abs(Math.sin(tick + i * 0.45));
        const bassBump = i < 8 ? (dsp.bass / 100) * 0.18 * (1 - i / 8) : 0;
        const level = Math.min(0.95, Math.max(0.06, norm * 0.65 + pulse + bassBump));
        const bh = level * (h - 24);
        const x = 20 + i * barW;
        const y = h - bh;

        const grad = ctx.createLinearGradient(0, y, 0, h);
        grad.addColorStop(0, "#00F0FF");
        grad.addColorStop(0.6, "#8B5CF6");
        grad.addColorStop(1, "rgba(0, 240, 255, 0.12)");
        ctx.fillStyle = grad;
        ctx.fillRect(x, y, barW - 5, bh);

        const curveY = h / 2 - (gVal / 15) * (h * 0.38);
        if (i === 0) ctx.moveTo(x + (barW - 5) / 2, curveY);
        else ctx.lineTo(x + (barW - 5) / 2, curveY);
      }
      ctx.strokeStyle = "#FF2A85";
      ctx.lineWidth = 2.5;
      ctx.stroke();

      requestAnimationFrame(drawSpectrum);
    }

    // 3D Multi-Instrument & Virtual 7.1.4 Dual-Woofer Stage Architect Interactive Engine
    const STAGE_NODES = [
      { id: "sub1", code: "SUB-1", title: 'Front 18" Subwoofer', band: "20Hz–90Hz Deep Sub-Bass Woofer Cone", az: -24, dist: 0.55, gain: 3.5, color: "#FF2A85", muted: false, solo: false },
      { id: "sub2", code: "SUB-2", title: 'Rear Trunk 15" Woofer', band: "30Hz–110Hz Rear Tactile LFE Woofer", az: 162, dist: 0.65, gain: 2.5, color: "#FF6D00", muted: false, solo: false },
      { id: "voc",  code: "VOC",   title: "Lead Vocals & Dialog", band: "300Hz–3.2kHz Center Presence Stem", az: 0, dist: 0.35, gain: 2.0, color: "#00F0FF", muted: false, solo: false },
      { id: "drm",  code: "DRM",   title: "Kick & Snare Kit", band: "60Hz–250Hz & 2.5kHz Attack", az: 18, dist: 0.48, gain: 2.0, color: "#FFB300", muted: false, solo: false },
      { id: "gtr",  code: "GTR",   title: "Acoustic & Lead Guitar", band: "250Hz–4kHz Harmonic Body", az: -48, dist: 0.52, gain: 1.5, color: "#00E676", muted: false, solo: false },
      { id: "syn",  code: "SYN",   title: "Synth Pad & Cymbal Air", band: "5kHz–20kHz 3D Shimmer Ribbon", az: 52, dist: 0.56, gain: 1.8, color: "#8B5CF6", muted: false, solo: false },
      { id: "spkl", code: "SPK-L", title: "Left Tower Monitor", band: "Full-Range Left Acoustic Array", az: -65, dist: 0.72, gain: 1.0, color: "#38BDF8", muted: false, solo: false },
      { id: "spkr", code: "SPK-R", title: "Right Tower Monitor", band: "Full-Range Right Acoustic Array", az: 65, dist: 0.72, gain: 1.0, color: "#38BDF8", muted: false, solo: false }
    ];
    let selectedNodeId = "sub1";
    let draggingNodeId = null;
    let wooferCrossover = 90;
    let wooferPhase180 = false;
    let vocalCenterLock = true;

    const stageCanvas = document.getElementById("stageCanvas");
    const sCtx = stageCanvas.getContext("2d");

    function getStageCoords(node) {
      const cx = stageCanvas.width / 2;
      const cy = stageCanvas.height / 2;
      const maxR = Math.min(cx, cy) - 24;
      const rad = ((node.az - 90) * Math.PI) / 180;
      const r = node.dist * maxR;
      return { x: cx + Math.cos(rad) * r, y: cy + Math.sin(rad) * r };
    }

    function updateSelectedUi() {
      const n = STAGE_NODES.find(x => x.id === selectedNodeId) || STAGE_NODES[0];
      document.getElementById("selNodeTitle").textContent = "Selected: " + n.title + " (" + n.code + ")";
      document.getElementById("selNodeCoords").textContent = "Azimuth " + Math.round(n.az) + "° • Dist " + Math.round(n.dist * 100) + "%";
      document.getElementById("selNodeBand").textContent = n.band;
      document.getElementById("selNodeGainVal").textContent = (n.gain >= 0 ? "+" : "") + n.gain.toFixed(1) + " dB";
      document.getElementById("selNodeGainSlider").value = n.gain;
      document.getElementById("muteNodeBtn").textContent = n.muted ? "🔊 Unmute Node" : "🔇 Mute Node";
      document.getElementById("soloNodeBtn").textContent = n.solo ? "🎯 Unsolo Node" : "🎯 Solo Node";
      recomputeStageEq();
    }

    function recomputeStageEq() {
      let subPan = 0, midPan = 0, airPan = 0;
      STAGE_NODES.forEach(n => {
        if (n.muted) return;
        const pan = Math.sin((n.az * Math.PI) / 180) * (1 - n.dist * 0.35);
        if (n.id.startsWith("sub") || n.id === "drm") subPan += pan * 35;
        else if (n.id === "voc" || n.id === "gtr") midPan += pan * 40;
        else airPan += pan * 40;
      });
      document.getElementById("stageTelemetry").textContent =
        "Stage Active • 2 Woofers (X-Over " + wooferCrossover + "Hz • Phase " + (wooferPhase180 ? "180°" : "0°") +
        ") • Sub L/R " + Math.round(subPan) + "% | Mid " + Math.round(midPan) + "% | Air " + Math.round(airPan) + "%";
    }

    function drawStageRadar() {
      sCtx.clearRect(0, 0, stageCanvas.width, stageCanvas.height);
      const cx = stageCanvas.width / 2;
      const cy = stageCanvas.height / 2;
      const maxR = Math.min(cx, cy) - 24;

      [0.33, 0.66, 1.0].forEach(f => {
        sCtx.beginPath();
        sCtx.arc(cx, cy, maxR * f, 0, Math.PI * 2);
        sCtx.strokeStyle = "#1b2436";
        sCtx.lineWidth = 1;
        sCtx.stroke();
      });

      sCtx.beginPath();
      sCtx.moveTo(cx, 10); sCtx.lineTo(cx, stageCanvas.height - 10);
      sCtx.moveTo(10, cy); sCtx.lineTo(stageCanvas.width - 10, cy);
      sCtx.strokeStyle = "#161e2e";
      sCtx.stroke();

      // Listener head
      sCtx.beginPath();
      sCtx.arc(cx, cy, 11, 0, Math.PI * 2);
      sCtx.fillStyle = "#101826";
      sCtx.fill();
      sCtx.strokeStyle = "#00F0FF";
      sCtx.lineWidth = 2;
      sCtx.stroke();
      sCtx.fillStyle = "#F4F7FF";
      sCtx.font = "bold 8px monospace";
      sCtx.textAlign = "center";
      sCtx.fillText("YOU", cx, cy + 3);

      STAGE_NODES.forEach(n => {
        const pos = getStageCoords(n);
        const isSel = n.id === selectedNodeId;

        if (n.id.startsWith("sub") && !n.muted) {
          const pulseR = 16 + Math.abs(Math.sin(tick * 1.4)) * 9;
          sCtx.beginPath();
          sCtx.arc(pos.x, pos.y, pulseR, 0, Math.PI * 2);
          sCtx.strokeStyle = n.color + "55";
          sCtx.lineWidth = 2;
          sCtx.stroke();
        }

        sCtx.beginPath();
        sCtx.moveTo(cx, cy);
        sCtx.lineTo(pos.x, pos.y);
        sCtx.strokeStyle = isSel ? n.color : "#232d42";
        sCtx.lineWidth = isSel ? 1.8 : 1;
        sCtx.stroke();

        sCtx.beginPath();
        sCtx.arc(pos.x, pos.y, isSel ? 15 : 12, 0, Math.PI * 2);
        sCtx.fillStyle = n.muted ? "#1f2430" : "#0b101b";
        sCtx.fill();
        sCtx.strokeStyle = n.muted ? "#525b70" : n.color;
        sCtx.lineWidth = isSel ? 2.6 : 1.6;
        sCtx.stroke();

        sCtx.fillStyle = n.muted ? "#68738c" : "#F4F7FF";
        sCtx.font = "bold 8px monospace";
        sCtx.textAlign = "center";
        sCtx.fillText(n.code, pos.x, pos.y + 3);
      });

      requestAnimationFrame(drawStageRadar);
    }

    function handleStagePointer(clientX, clientY, isStart) {
      const rect = stageCanvas.getBoundingClientRect();
      const scaleX = stageCanvas.width / rect.width;
      const scaleY = stageCanvas.height / rect.height;
      const px = (clientX - rect.left) * scaleX;
      const py = (clientY - rect.top) * scaleY;

      if (isStart) {
        let best = null, bestD = 999;
        STAGE_NODES.forEach(n => {
          const p = getStageCoords(n);
          const d = Math.hypot(px - p.x, py - p.y);
          if (d < bestD) { bestD = d; best = n; }
        });
        if (best && bestD <= 28) {
          selectedNodeId = best.id;
          draggingNodeId = best.id;
          updateSelectedUi();
        }
        return;
      }

      if (!draggingNodeId) return;
      const n = STAGE_NODES.find(x => x.id === draggingNodeId);
      if (!n) return;
      const cx = stageCanvas.width / 2;
      const cy = stageCanvas.height / 2;
      const maxR = Math.min(cx, cy) - 24;
      const dx = px - cx;
      const dy = py - cy;
      let az = (Math.atan2(dy, dx) * 180) / Math.PI + 90;
      if (az > 180) az -= 360;
      if (az < -180) az += 360;
      n.az = vocalCenterLock && n.id === "voc" ? 0 : az;
      n.dist = Math.max(0.08, Math.min(1.0, Math.hypot(dx, dy) / maxR));
      updateSelectedUi();
    }

    stageCanvas.addEventListener("pointerdown", e => {
      stageCanvas.setPointerCapture(e.pointerId);
      handleStagePointer(e.clientX, e.clientY, true);
    });
    stageCanvas.addEventListener("pointermove", e => {
      if (draggingNodeId) handleStagePointer(e.clientX, e.clientY, false);
    });
    stageCanvas.addEventListener("pointerup", () => { draggingNodeId = null; });

    function updateSelectedNodeGain(v) {
      const n = STAGE_NODES.find(x => x.id === selectedNodeId);
      if (!n) return;
      n.gain = parseFloat(v);
      updateSelectedUi();
    }
    function toggleSelectedMute() {
      const n = STAGE_NODES.find(x => x.id === selectedNodeId);
      if (!n) return;
      n.muted = !n.muted;
      updateSelectedUi();
    }
    function toggleSelectedSolo() {
      const n = STAGE_NODES.find(x => x.id === selectedNodeId);
      if (!n) return;
      n.solo = !n.solo;
      STAGE_NODES.forEach(o => { if (o.id !== n.id) o.muted = n.solo; });
      if (!n.solo) STAGE_NODES.forEach(o => { o.muted = false; });
      updateSelectedUi();
    }
    function centerSelectedNode() {
      const n = STAGE_NODES.find(x => x.id === selectedNodeId);
      if (!n) return;
      n.az = 0; n.dist = 0.25;
      updateSelectedUi();
    }
    function updateCrossover(hz) {
      wooferCrossover = parseInt(hz, 10);
      document.getElementById("xoverVal").textContent = wooferCrossover + " Hz • " + (wooferPhase180 ? "180°" : "0°") + " Phase";
      recomputeStageEq();
    }
    function toggleWooferPhase() {
      wooferPhase180 = !wooferPhase180;
      document.getElementById("phaseBtn").textContent = "⚡ Woofer Phase: " + (wooferPhase180 ? "180° Inverted" : "0° Normal");
      updateCrossover(wooferCrossover);
    }
    function toggleVocalLock() {
      vocalCenterLock = !vocalCenterLock;
      document.getElementById("vocalLockBtn").textContent = "🎤 Center Vocal Lock: " + (vocalCenterLock ? "ON" : "OFF");
      if (vocalCenterLock) {
        const voc = STAGE_NODES.find(x => x.id === "voc");
        if (voc) voc.az = 0;
      }
      updateSelectedUi();
    }
    function setStageFormation(key) {
      const presets = {
        arena:   { sub1: [-24,0.55,3.5], sub2: [162,0.65,2.5], voc: [0,0.32,2.2], drm: [18,0.48,2.0], gtr: [-48,0.52,1.5], syn: [52,0.56,1.8], spkl: [-65,0.72,1.2], spkr: [65,0.72,1.2] },
        drummer: { sub1: [0,0.22,4.5],   sub2: [180,0.35,4.0], voc: [0,0.58,1.5], drm: [0,0.14,4.2],  gtr: [-75,0.62,1.8], syn: [75,0.62,2.0], spkl: [-95,0.78,1.5], spkr: [95,0.78,1.5] },
        maybach: { sub1: [-35,0.42,5.0], sub2: [145,0.48,4.8], voc: [0,0.25,2.8], drm: [12,0.40,2.5], gtr: [-55,0.46,2.0], syn: [55,0.46,2.4], spkl: [-45,0.55,2.2], spkr: [45,0.55,2.2] },
        club360: { sub1: [-90,0.35,6.0], sub2: [90,0.35,6.0],  voc: [0,0.40,1.8], drm: [180,0.42,3.8],gtr: [-135,0.65,2.0],syn: [135,0.65,2.8],spkl: [-45,0.80,2.5], spkr: [45,0.80,2.5] },
        split:   { sub1: [-85,0.45,4.2], sub2: [-110,0.55,3.8],voc: [0,0.20,3.2], drm: [-70,0.48,3.0],gtr: [75,0.48,3.0],  syn: [95,0.55,3.2], spkl: [-90,0.75,2.0], spkr: [90,0.75,2.0] }
      };
      const cfg = presets[key];
      if (!cfg) return;
      STAGE_NODES.forEach(n => {
        if (cfg[n.id]) {
          n.az = cfg[n.id][0];
          n.dist = cfg[n.id][1];
          n.gain = cfg[n.id][2];
          n.muted = false;
          n.solo = false;
        }
      });
      updateSelectedUi();
    }

    function applyStemMode(mode) {
      const map = {
        balanced: { label: "100% Balanced", delta: [0,0,0] },
        karaoke:  { label: "Karaoke (Vocals Cut -9dB)", delta: [3.5, -8.5, 3.0] },
        vocal2x:  { label: "200% Vocal Spotlight", delta: [-2.5, 7.5, -2.0] },
        dnb:      { label: "Drum & 808 Bass Solo", delta: [6.5, -5.5, -3.5] }
      };
      const m = map[mode] || map.balanced;
      for (let i = 0; i < 31; i++) {
        const d = i <= 8 ? m.delta[0] : (i <= 21 ? m.delta[1] : m.delta[2]);
        gains[i] = Math.max(-15, Math.min(15, Math.round((gains[i] * 0.5 + d) * 2) / 2));
      }
      document.getElementById("hyperMatrixStatus").textContent = "🎚️ 4-STEM DE-MIXER: " + m.label + " Applied to 31-Band Curve";
      renderBands();
    }
    function setGyro(lbl) {
      document.getElementById("gyroVal").textContent = lbl;
      document.getElementById("hyperMatrixStatus").textContent = "🧭 4D GYRO HEAD-TRACKING: " + lbl + " Active";
    }
    function updateSubSynth(v) {
      document.getElementById("subSynthVal").textContent = v + "% • HAPTIC ON";
      for (let i = 0; i < 6; i++) {
        gains[i] = Math.max(-15, Math.min(15, Math.round(((v / 100) * 8.5 - i * 0.5) * 2) / 2));
      }
      renderBands();
    }
    function setCruiseTube(lbl) {
      document.getElementById("cruiseTubeVal").textContent = lbl;
      document.getElementById("hyperMatrixStatus").textContent = "🏎️ SPEED CRUISE & ANALOG: " + lbl + " Engaged";
    }
    function toggleTinnitusWeb() {
      gains[25] = -6.5; gains[26] = -4.5;
      document.getElementById("hyperMatrixStatus").textContent = "🛡️ TINNITUS NOTCH-SHIELD: 7.5kHz–8kHz Harshness Suppressed (-6.5 dB)";
      renderBands();
    }
    function setVenueWeb(name) {
      currentPreset = "Venue: " + name;
      gains = [8.2,8.5,8.6,7.8,6.8,5.5,4.0,2.5,1.2,0.2,-0.5,-0.8,-0.5,0.2,1.0,1.8,2.6,3.2,3.6,3.4,3.0,2.8,3.2,3.8,4.2,4.5,4.2,3.6,3.0,2.4,1.8];
      PRESETS[currentPreset] = [...gains];
      document.getElementById("hyperMatrixStatus").textContent = "🏛️ VENUE IMPULSE CLONED: " + name;
      renderPresets();
      renderBands();
    }
    function captureRefDna() {
      currentPreset = "DNA: Cloned Reference Mix";
      PRESETS[currentPreset] = [...gains];
      document.getElementById("hyperMatrixStatus").textContent = "🧬 REFERENCE TRACK DNA LOCKED • 31-Band Spectral Envelope Cloned!";
      renderPresets();
    }

    let oohDist = 3.0;
    let oohRef = 58;
    let oohPinna = true;
    let oohFloorSub = true;
    let oohLabel = "3.0m Living Room 5.1 Sofa";

    function applyOohCurve(modeKey) {
      const distNorm = (oohDist - 0.5) / 5.5;
      const refNorm = oohRef / 100.0;
      for (let i = 0; i < 31; i++) {
        let g = gains[i] * 0.55;
        if (oohFloorSub && i <= 7) g += 3.5 + refNorm * 2.0;
        if (oohPinna) {
          if (i >= 9 && i <= 11) g += 2.2;
          if (i === 12 || i === 13) g -= 1.4;
          if (i >= 19 && i <= 22) g += 2.0;
          if (i === 25 || i === 26) g -= 4.4;
        }
        if (i >= 27) g -= distNorm * 3.5;
        if (modeKey === 'nextroom' && i >= 20) g -= (i - 19) * 1.1;
        gains[i] = Math.max(-15, Math.min(15, Math.round(g * 2) / 2));
      }
      document.getElementById("oohStatus").textContent =
        "🔊 OUT-OF-HEAD [" + oohLabel.toUpperCase() + "]: " + oohDist.toFixed(1) +
        "m Throw • Wall Bounce " + oohRef + "% • ITD 340µs Crossfeed • " +
        (oohPinna ? "Pinna 8kHz Notch ON" : "Pinna OFF") + " • " +
        (oohFloorSub ? "Floor Sub-Wave ON" : "Sub-Wave OFF");
      renderBands();
    }

    function setOohMode(key, label, dist, ref) {
      oohLabel = label;
      oohDist = dist;
      oohRef = ref;
      document.getElementById("oohDistVal").textContent = dist.toFixed(1) + " Meters";
      document.getElementById("oohDistSlider").value = dist;
      document.getElementById("oohRefVal").textContent = ref + "% Room Air";
      document.getElementById("oohRefSlider").value = ref;
      applyOohCurve(key);
    }
    function updateOohDist(v) {
      oohDist = parseFloat(v);
      document.getElementById("oohDistVal").textContent = oohDist.toFixed(1) + " Meters";
      applyOohCurve('custom');
    }
    function updateOohReflection(v) {
      oohRef = parseInt(v, 10);
      document.getElementById("oohRefVal").textContent = oohRef + "% Room Air";
      applyOohCurve('custom');
    }
    function toggleOohPinna() {
      oohPinna = !oohPinna;
      document.getElementById("pinnaBtn").textContent = "👂 Pinna 8kHz & Chest HRTF: " + (oohPinna ? "ON" : "OFF");
      applyOohCurve('custom');
    }
    function toggleOohFloorSub() {
      oohFloorSub = !oohFloorSub;
      document.getElementById("floorSubBtn").textContent = "🔊 Floor Sub-Wave: " + (oohFloorSub ? "ON" : "OFF");
      applyOohCurve('custom');
    }

    let triadJp = 85;
    let triadCn = 85;
    let triadUs = 85;
    let triadIso = true;
    let triadLabel = "🇯🇵🇨🇳🇺🇸 Triad Omega Fusion";

    function applyTriadCurve() {
      const jp = triadJp / 100.0;
      const cn = triadCn / 100.0;
      const us = triadUs / 100.0;
      for (let i = 0; i < 31; i++) {
        let g = gains[i] * 0.45;
        if (i <= 5) g += cn * 4.8 + us * 3.8 + (triadIso ? 2.2 : 0);
        if (i >= 6 && i <= 9) g += cn * 2.4 - us * 1.1;
        if (i >= 11 && i <= 15) g += cn * 2.1 + jp * 0.8;
        if (i >= 18 && i <= 22) g += jp * 2.8 + us * 2.5;
        if (i >= 24 && i <= 27) g += jp * 3.8 + cn * 2.4;
        if (i >= 28) g += jp * 5.2 + cn * 4.0 + us * 2.2 + (triadIso ? 1.5 : 0);
        gains[i] = Math.max(-15, Math.min(15, Math.round(g * 2) / 2));
      }
      document.getElementById("triadStatus").textContent =
        "🌐 [" + triadLabel.toUpperCase() + "] • 🇯🇵 DSEE " + triadJp + "% • 🇨🇳 R2R+Be " + triadCn +
        "% • 🇺🇸 Dirac+Sphere " + triadUs + "% • " + (triadIso ? "ISO 226 Loudness ON" : "ISO 226 OFF");
      renderBands();
    }

    function setTriadMode(key, label, jp, cn, us) {
      triadLabel = label;
      triadJp = jp;
      triadCn = cn;
      triadUs = us;
      document.getElementById("jpDseeVal").textContent = jp + "%";
      document.getElementById("jpDseeSlider").value = jp;
      document.getElementById("cnR2rVal").textContent = cn + "%";
      document.getElementById("cnR2rSlider").value = cn;
      document.getElementById("usDiracVal").textContent = us + "%";
      document.getElementById("usDiracSlider").value = us;
      applyTriadCurve();
    }

    function updateTriadSlider(which, val) {
      const v = parseInt(val, 10);
      if (which === 'jp') { triadJp = v; document.getElementById("jpDseeVal").textContent = v + "%"; }
      if (which === 'cn') { triadCn = v; document.getElementById("cnR2rVal").textContent = v + "%"; }
      if (which === 'us') { triadUs = v; document.getElementById("usDiracVal").textContent = v + "%"; }
      applyTriadCurve();
    }

    function toggleIsoLoudness() {
      triadIso = !triadIso;
      document.getElementById("isoLoudnessBtn").textContent = "📈 ISO 226 Dynamic Equal-Loudness: " + (triadIso ? "ON" : "OFF");
      applyTriadCurve();
    }

    let rndP16 = 88;
    let rndDsd = 90;
    let rndWfs = 92;
    let rndSq = 86;
    let rndDoppler = true;
    let rndDeEss = true;
    let rndLabel = "🛸 2030 Singularity";

    function applyRnd2030Curve() {
      const p16 = rndP16 / 100.0;
      const dsd = rndDsd / 100.0;
      const wfs = rndWfs / 100.0;
      const sq = rndSq / 100.0;
      for (let i = 0; i < 31; i++) {
        let g = gains[i] * 0.45;
        if (i >= 1 && i <= 6) g += p16 * 4.8 + dsd * 3.0 + sq * 3.2;
        if (i >= 7 && i <= 11) g -= (rndDoppler ? 2.2 : 0) + dsd * 1.4;
        if (i >= 13 && i <= 17) g += dsd * 2.0 + wfs * 2.1;
        if (i >= 18 && i <= 23) g += (rndDoppler ? 2.8 : 0) + wfs * 2.6 + sq * 2.5;
        if (i === 25 || i === 26) g -= (rndDeEss ? 3.5 : 0);
        if (i >= 27) g += dsd * 3.8 + (rndDeEss ? 2.2 : 0);
        gains[i] = Math.max(-15, Math.min(15, Math.round(g * 2) / 2));
      }
      const sqDb = ((rndSq / 100.0) * 6.0).toFixed(1);
      document.getElementById("rndWfsVal").textContent = rndWfs + "% | +" + sqDb + "dB";
      document.getElementById("rnd2030Status").textContent =
        "🛸 [" + rndLabel.toUpperCase() + "] • 🇯🇵 16Hz Phantom " + rndP16 + "% • " +
        (rndDoppler ? "Doppler-0 ON" : "Doppler OFF") + " • 🇨🇳 DSD1024 " + rndDsd + "% • " +
        (rndDeEss ? "Bio De-Esser ON" : "De-Esser OFF") + " • 🇺🇸 64-WFS " + rndWfs + "% • De-Squash +" + sqDb + "dB";
      renderBands();
    }

    function setRnd2030Mode(key, label, p16, dsd, wfs, sq) {
      rndLabel = label;
      rndP16 = p16;
      rndDsd = dsd;
      rndWfs = wfs;
      rndSq = sq;
      document.getElementById("rnd16Val").textContent = p16 + "%";
      document.getElementById("rnd16Slider").value = p16;
      document.getElementById("rndDsdVal").textContent = dsd + "%";
      document.getElementById("rndDsdSlider").value = dsd;
      document.getElementById("rndWfsSlider").value = wfs;
      document.getElementById("rndSqSlider").value = sq;
      applyRnd2030Curve();
    }

    function updateRnd2030Slider(which, val) {
      const v = parseInt(val, 10);
      if (which === 'p16') { rndP16 = v; document.getElementById("rnd16Val").textContent = v + "%"; }
      if (which === 'dsd') { rndDsd = v; document.getElementById("rndDsdVal").textContent = v + "%"; }
      if (which === 'wfs') { rndWfs = v; }
      if (which === 'sq') { rndSq = v; }
      applyRnd2030Curve();
    }

    function toggleRndDoppler() {
      rndDoppler = !rndDoppler;
      document.getElementById("dopplerBtn").textContent = "🫧 Yamaha Inverse Doppler Vocal Eraser: " + (rndDoppler ? "ON" : "OFF");
      applyRnd2030Curve();
    }

    function toggleRndDeEsser() {
      rndDeEss = !rndDeEss;
      document.getElementById("deEssBtn").textContent = "🧬 Biometric Ear-Canal 7kHz De-Esser: " + (rndDeEss ? "ON" : "OFF");
      applyRnd2030Curve();
    }

    renderPresets();
    renderBands();
    updateSelectedUi();
    drawSpectrum();
    drawStageRadar();
  </script>
</body>
</html>`;

const server = http.createServer((req, res) => {
  const url = (req.url || '/').split('?')[0];

  if (url === '/health' || url === '/api/health') {
    const apk = getApkPath();
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({ status: 'ok', apkReady: Boolean(apk), apkPath: apk }));
    return;
  }

  if (url.startsWith('/download/') || url.endsWith('.apk')) {
    const apk = getApkPath();
    if (!apk) {
      res.writeHead(404, { 'Content-Type': 'text/plain' });
      res.end('APK not found. Please run a build first.');
      return;
    }
    const stat = fs.statSync(apk);
    res.writeHead(200, {
      'Content-Type': 'application/vnd.android.package-archive',
      'Content-Length': stat.size,
      'Content-Disposition': 'attachment; filename="Equltra-v1.0-PRO.apk"',
    });
    fs.createReadStream(apk).pipe(res);
    return;
  }

  res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
  res.end(HTML_PAGE);
});

server.listen(PORT, '0.0.0.0', () => {
  console.log('Equltra Quantum Lab preview & APK server listening on http://0.0.0.0:' + PORT);
});
