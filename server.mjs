import http from 'node:http';
import fs from 'node:fs';
import path from 'node:path';

const PORT = 3000;
const APK_PATHS = [
  '/app/applet/app/build/outputs/apk/debug/app-debug.apk',
  '/app/applet/.build-outputs/app-debug.apk',
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
  <title>Equltra — 31-Band ISO Studio EQ & Android DSP</title>
  <meta name="description" content="Equltra - 31-Band ISO 1/3-Octave System-wide EQ with YouTube Hook, AI Auto-EQ PRO, Voice Control, Bass Boost & Spatial DSP." />
  <meta property="og:title" content="Equltra — 31-Band ISO Studio EQ & Android DSP" />
  <meta property="og:description" content="Equltra - 31-Band ISO 1/3-Octave System-wide EQ with YouTube Hook, AI Auto-EQ PRO, Voice Control, Bass Boost & Spatial DSP." />
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
    .brand {
      display: flex;
      align-items: center;
      gap: 12px;
    }
    .logo-badge {
      width: 38px;
      height: 38px;
      border-radius: 10px;
      background: linear-gradient(135deg, var(--cyan), var(--magenta));
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: 900;
      color: #050608;
      font-size: 16px;
      letter-spacing: -0.5px;
    }
    .brand h1 {
      font-size: 18px;
      font-weight: 800;
      letter-spacing: 0.6px;
    }
    .brand p {
      font-size: 11px;
      color: var(--cyan);
    }
    .header-actions {
      display: flex;
      align-items: center;
      gap: 10px;
      flex-wrap: wrap;
    }
    .btn {
      border: 1px solid var(--border);
      background: var(--card);
      color: var(--text);
      padding: 8px 14px;
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
      background: linear-gradient(90deg, var(--cyan), #00b8ff);
      color: #040608;
      border: none;
    }
    .btn-magenta {
      background: linear-gradient(90deg, var(--magenta), var(--violet));
      color: #fff;
      border: none;
    }
    main {
      max-width: 1220px;
      width: 100%;
      margin: 0 auto;
      padding: 16px;
      display: flex;
      flex-direction: column;
      gap: 14px;
      flex: 1;
    }
    .status-strip {
      background: var(--surface);
      border: 1px solid var(--border);
      border-radius: 10px;
      padding: 10px 14px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-wrap: wrap;
      gap: 10px;
      font-size: 12px;
    }
    .dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: var(--emerald);
      display: inline-block;
      box-shadow: 0 0 8px var(--emerald);
      margin-right: 6px;
    }
    .card {
      background: var(--card);
      border: 1px solid var(--border);
      border-radius: 12px;
      padding: 14px;
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
    canvas {
      width: 100%;
      height: 170px;
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
      font-size: 12px;
      font-weight: 600;
      cursor: pointer;
      white-space: nowrap;
    }
    .chip.active {
      background: rgba(0, 240, 255, 0.14);
      border-color: var(--cyan);
      color: var(--cyan);
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
    .band-val {
      font-size: 10px;
      color: var(--cyan);
      font-family: monospace;
    }
    .band-slider {
      writing-mode: vertical-lr;
      direction: rtl;
      height: 130px;
      width: 18px;
      accent-color: var(--cyan);
      cursor: pointer;
    }
    .band-freq {
      font-size: 10px;
      color: var(--muted);
      font-family: monospace;
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
    .knob-box input[type=range] {
      width: 100%;
      accent-color: var(--magenta);
    }
  </style>
</head>
<body>
  <header>
    <div class="brand">
      <div class="logo-badge">EQ</div>
      <div>
        <h1>EQULTRA 31-BAND PRO</h1>
        <p>ISO 1/3-Octave System-Wide Android DSP • YouTube &amp; Global Session Hook</p>
      </div>
    </div>
    <div class="header-actions">
      <button class="btn" id="testAudioBtn" onclick="toggleSynth()">▶ Play Test Synth</button>
      <button class="btn btn-magenta" onclick="applyAiPreset()">✨ AI Auto-EQ</button>
      <a class="btn btn-primary" href="/download/Equltra-v1.0-debug.apk" download="Equltra-v1.0-debug.apk">⬇ Download Android APK</a>
    </div>
  </header>

  <main>
    <div class="status-strip">
      <div><span class="dot"></span><strong>Global YouTube &amp; System DSP Active</strong> (31-Band DynamicsProcessing + LoudnessEnhancer)</div>
      <div id="apkStatus">APK Build: Ready (app-debug.apk)</div>
    </div>

    <div class="card">
      <div class="card-header">
        <span class="card-title">Real-Time 31-Band RTA Spectrum &amp; Frequency Curve</span>
        <span id="telemetryText" style="font-size:11px;color:var(--muted);font-family:monospace;">Dominant: 125 Hz | Peak: -6.4 dBFS | Preamp: -2.0 dB</span>
      </div>
      <canvas id="spectrumCanvas" width="1100" height="170"></canvas>
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
        <span style="font-size:11px;color:var(--muted);">Scroll horizontally to sculpt all 31 bands (20Hz – 20kHz)</span>
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
    let dsp = { bass: 78, spatial: 35, reverb: 12, preamp: -2.0 };
    let playing = false;
    let audioCtx = null;
    let synthInterval = null;
    let tick = 0;

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

    function applyAiPreset() {
      currentPreset = "AI Harman Pro";
      gains = gains.map((_, i) => {
        if (i < 7) return +(5.5 - i * 0.4).toFixed(1);
        if (i < 14) return +(-1.2 + Math.sin(i) * 0.8).toFixed(1);
        return +(2.2 + Math.cos(i * 0.5) * 1.6).toFixed(1);
      });
      PRESETS[currentPreset] = [...gains];
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
      if (!audioCtx) audioCtx = new (window.AudioContext || window.webkitAudioContext)();
      let step = 0;
      const notes = [55, 55, 65.41, 73.42, 110, 146.83, 220, 329.63];
      synthInterval = setInterval(() => {
        if (!playing || !audioCtx) return;
        const osc = audioCtx.createOscillator();
        const gain = audioCtx.createGain();
        const freq = notes[step % notes.length];
        osc.type = step % 2 === 0 ? "sawtooth" : "triangle";
        osc.frequency.setValueAtTime(freq, audioCtx.currentTime);
        const subBoost = 1 + (dsp.bass / 100) * 0.6;
        gain.gain.setValueAtTime(0.08 * subBoost, audioCtx.currentTime);
        gain.gain.exponentialRampToValueAtTime(0.001, audioCtx.currentTime + 0.22);
        osc.connect(gain);
        gain.connect(audioCtx.destination);
        osc.start();
        osc.stop(audioCtx.currentTime + 0.23);
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
        const norm = (gains[i] + 15) / 30;
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

        const curveY = h / 2 - (gains[i] / 15) * (h * 0.38);
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
      'Content-Disposition': 'attachment; filename="Equltra-v1.0-debug.apk"',
    });
    fs.createReadStream(apk).pipe(res);
    return;
  }

  res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
  res.end(HTML_PAGE);
});

server.listen(PORT, '0.0.0.0', () => {
  console.log('Equltra preview & APK server listening on http://0.0.0.0:' + PORT);
});
