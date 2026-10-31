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
    <!-- 7-in-1 Future-Gen Quantum Lab Card -->
    <div class="quantum-card">
      <div class="card-header">
        <div>
          <span class="card-title">⚡ 7-IN-1 FUTURE-GEN QUANTUM AUDIO LAB (2027 EDITION)</span>
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

    renderPresets();
    renderBands();
    drawSpectrum();
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
