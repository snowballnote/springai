import { useState } from 'react'

type Msg = {
    text: string;
    role: 'user' | 'ai';
}

export default function App() {
    const [input, setInput] = useState("");
    const[messages, setMessages] = useState<Msg[]>([]);
    const [mode, setMode] = useState<"/test" | "/test2">("/test");

    const send = async () => {
        if(!input.trim()) return;

        const userMsg: Msg = {role: "user", text: input};
        setMessages((prev) => [...prev, userMsg]);
        setInput("");

        const res = await fetch(mode, {
            method: "POST", 
            headers: {
                "Content-Type": "text/plain"
            }, 
            body: userMsg.text, 
        });

        const aiText = await res.text();
        setMessages((prev) => [...prev, {role: "ai", text: aiText}]);
    };  

    return (
        <>
            <div style={{ maxWidth: 500, margin: "40px auto" }}>
                <h2>Spring AI Test</h2>

                <select value={mode} onChange={(e) => setMode(e.target.value as any)}>
                    <option value="/test">조폭</option>
                    <option value="/test2">유치원</option>
                </select>

                <div
                    style={{
                        border: "1px solid #ddd",
                        height: 300,
                        overflowY: "auto",
                        padding: 10,
                        marginTop: 10,
                    }}
                >
                    {messages.map((m, i) => (
                        <div key={i} style={{ marginBottom: 8 }}>
                            <b>{m.role === "user" ? "나" : "AI"}:</b> {m.text}
                        </div>
                    ))}
                </div>

                <input
                    value={input}
                    onChange={(e) => setInput(e.target.value)}
                    onKeyDown={(e) => e.key === "Enter" && send()}
                    placeholder="메시지 입력"
                    style={{ width: "100%", marginTop: 10 }}
                />

                <button onClick={send} style={{ marginTop: 10 }}>
                    전송
                </button>
            </div>
        </>
    );
}


