import { useState, useEffect, useRef } from "react";

type Msg = {
  text: string;
  role: "user" | "ai";
};

export default function App() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState<Msg[]>([]);
  const [mode, setMode] = useState<"/chatMessage" | "/chatMessage2">(
    "/chatMessage"
  );

  const chatRef = useRef<HTMLDivElement>(null);

  // 최초 인사
  useEffect(() => {
    setMessages([
      {
        role: "ai",
        text: "안녕하세요! 저는 고객센터 챗봇입니다.\n궁금한 점이 있으시면 언제든지 물어보세요 😊",
      },
    ]);
  }, []);

  // 스크롤 항상 아래로
  useEffect(() => {
    chatRef.current?.scrollTo({
      top: chatRef.current.scrollHeight,
      behavior: "smooth",
    });
  }, [messages]);

  const send = async () => {
    if (!input.trim()) return;

    const userId = "test";

    const res = await fetch(mode, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        userId,
        userMessage: input,
      }),
    });

    const result = await res.json();

    setMessages((prev) => [
      ...prev,
      ...result.map((r: any) => ({
        role: r.who === "YOU" ? "user" : "ai",
        text: r.message,
      })),
    ]);

    setInput("");
  };

  return (
    <div
      style={{
        backgroundColor: "#f2f2f2",
        minHeight: "100vh",
        paddingTop: 20,
        fontFamily: "Arial, sans-serif",
      }}
    >
      <h1 style={{ textAlign: "center" }}>쇼핑몰 Chat Bot</h1>

      <div style={{ width: 420, margin: "20px auto" }}>
        {/* 채팅 영역 */}
        <div
          ref={chatRef}
          style={{
            height: 450,
            backgroundColor: "#b2c7da",
            padding: 15,
            overflowY: "auto",
            borderRadius: 12,
            boxSizing: "border-box",
          }}
        >
          {messages.map((m, i) => (
            <div
              key={i}
              style={{
                display: "flex",
                justifyContent:
                  m.role === "user" ? "flex-end" : "flex-start",
                marginBottom: 12,
              }}
            >
              <div
                style={{
                  maxWidth: "70%",
                  padding: "10px 14px",
                  borderRadius: 18,
                  lineHeight: 1.4,
                  wordBreak: "break-word",
                  backgroundColor:
                    m.role === "user" ? "#fff48f" : "#ffffff",
                  borderBottomRightRadius:
                    m.role === "user" ? 4 : 18,
                  borderBottomLeftRadius:
                    m.role === "ai" ? 4 : 18,
                }}
              >
                {m.text}
              </div>
            </div>
          ))}
        </div>

        {/* 입력 영역 */}
        <div
          style={{
            display: "flex",
            marginTop: 10,
            gap: 8,
          }}
        >
          <input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && send()}
            placeholder="메시지를 입력하세요"
            style={{
              flex: 1,
              padding: "10px 14px",
              borderRadius: 20,
              border: "1px solid #ccc",
              fontSize: 14,
              outline: "none",
            }}
          />
          <button
            onClick={send}
            style={{
              padding: "10px 18px",
              borderRadius: 20,
              border: "none",
              backgroundColor: "#ffeb3b",
              fontWeight: "bold",
              cursor: "pointer",
            }}
          >
            전송
          </button>
        </div>
      </div>
    </div>
  );
}
