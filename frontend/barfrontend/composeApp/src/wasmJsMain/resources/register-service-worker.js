window.addEventListener("load", () => {
    if (!("serviceWorker" in navigator)) {
        return;
    }

    navigator.serviceWorker.register("/service-worker.js").catch((error) => {
        console.error("Service worker registration failed", error);
    });
});
