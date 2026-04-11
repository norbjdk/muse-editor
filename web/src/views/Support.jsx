import { useState } from 'react';

function Support() {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        subject: '',
        message: ''
    });
    const [submitted, setSubmitted] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();
        setSubmitted(true);
        setTimeout(() => setSubmitted(false), 3000);
    };

    const faqs = [
        { q: "How do I download sheet music?", a: "Simply browse our library, click on any score, and press the 'Download' button. Scores are available as PDF files." },
        { q: "Is MUSE free to use?", a: "Yes! All basic features and public domain scores are completely free. Premium features are available with a subscription." },
        { q: "Can I upload my own compositions?", a: "Absolutely! If you register as an Artist, you can upload and share your original sheet music with the community." },
        { q: "What file formats are supported?", a: "We support PDF, MusicXML, MIDI, and our native MUSE format for interactive scores." },
    ];

    return (
        <div className="min-h-auto mt-15 bg-[#E8E4DD] pt-24 pb-12 px-4">
            <div className="max-w-6xl mx-auto">
                <div className="text-center mb-12">
                    <h1 className="text-4xl font-bold text-[#050505] mb-4">Support Center</h1>
                    <p className="text-lg text-[#2c2c2c] max-w-2xl mx-auto">
                        Need help? We're here for you. Browse our FAQs or send us a message.
                    </p>
                </div>

                <div className="grid lg:grid-cols-2 gap-8 mb-12">
                    <div className="bg-[#f7f4ee] rounded-2xl border border-[#CCC5B9] p-8">
                        <h2 className="text-2xl font-bold text-[#050505] mb-6">Frequently Asked Questions</h2>
                        <div className="space-y-6">
                            {faqs.map((faq, index) => (
                                <div key={index}>
                                    <h3 className="font-semibold text-[#050505] mb-2">{faq.q}</h3>
                                    <p className="text-[#2c2c2c] text-sm">{faq.a}</p>
                                </div>
                            ))}
                        </div>
                    </div>

                    <div className="bg-[#f7f4ee] rounded-2xl border border-[#CCC5B9] p-8">
                        <h2 className="text-2xl font-bold text-[#050505] mb-6">Contact Us</h2>
                        {submitted ? (
                            <div className="bg-[#365603]/10 border border-[#365603] rounded-lg p-4 text-center">
                                <p className="text-[#365603] font-semibold">✓ Message sent successfully!</p>
                                <p className="text-sm text-[#2c2c2c] mt-1">We'll get back to you within 24 hours.</p>
                            </div>
                        ) : (
                            <form onSubmit={handleSubmit} className="space-y-4">
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Name</label>
                                    <input
                                        type="text"
                                        required
                                        value={formData.name}
                                        onChange={(e) => setFormData({...formData, name: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Email</label>
                                    <input
                                        type="email"
                                        required
                                        value={formData.email}
                                        onChange={(e) => setFormData({...formData, email: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Subject</label>
                                    <input
                                        type="text"
                                        required
                                        value={formData.subject}
                                        onChange={(e) => setFormData({...formData, subject: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                    />
                                </div>
                                <div>
                                    <label className="block text-sm font-medium text-[#050505] mb-2">Message</label>
                                    <textarea
                                        rows="4"
                                        required
                                        value={formData.message}
                                        onChange={(e) => setFormData({...formData, message: e.target.value})}
                                        className="w-full px-4 py-2 bg-[#E8E4DD] border border-[#CCC5B9] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#365603] text-[#050505]"
                                    ></textarea>
                                </div>
                                <button
                                    type="submit"
                                    className="w-full px-6 py-3 bg-[#365603] text-[#f7f4ee] rounded-full hover:bg-[#213106] transition-all font-semibold"
                                >
                                    Send Message
                                </button>
                            </form>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Support;